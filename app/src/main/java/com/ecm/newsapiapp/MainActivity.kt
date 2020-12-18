package com.ecm.newsapiapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray


class MainActivity : AppCompatActivity(), RecyclerAdapter.OnArticleListener {

    private val API_KEY = "d31f5fa5f03443dd8a1b9e3fde92ec34"
    private val LANG = "fr"
    private val API_URL_SOURCES = "https://newsapi.org/v2/sources?apiKey=$API_KEY&language=$LANG"
    private val API_URL = "https://newsapi.org/v2/everything?apiKey=$API_KEY&language=$LANG"

    var sources = JSONArray()
    var selectedSource = ""

    var articlesData = ArrayList<Article>()
    var articlesLiveData = MutableLiveData<ArrayList<Article>>()

    lateinit var queue: RequestQueue
    lateinit var progressBar: ProgressBar
    lateinit var alertBox: AlertDialog.Builder
    lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        queue = Volley.newRequestQueue(this)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        sharedPref = getPreferences(Context.MODE_PRIVATE)
        setUpAlertDialogBuilder()
        requestSourcesFromAPI()



        val recyclerAdapter = RecyclerAdapter(articlesData, this)
        val recyclerView: RecyclerView = findViewById(R.id.rv_recyclerView)

        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        articlesLiveData.observe(this, Observer { it?.let {
            recyclerAdapter.submitList(it.toMutableList())
        } })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        if (sources.length() > 0) {
            menu?.clear()
        }
        for (index in 0 until sources.length()) {
            menu?.add(0, index, index, sources.getJSONObject(index).getString("name"))
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        selectedSource = sources.getJSONObject(item.itemId).getString("id")
        requestArticlesFromAPI(selectedSource)
        return true
    }

    private fun setUpAlertDialogBuilder() {
        alertBox = this.let {
            AlertDialog.Builder(it)
        }
        alertBox.setTitle("Un problème sauvage apparait !")
            .apply{
                setPositiveButton("Retry") { _, _ ->
                    requestSourcesFromAPI()
                }
            }
    }

    private fun showAlertDialog(message: String) {
        alertBox.setMessage(message)
        val alertDialog: AlertDialog = alertBox.create()
        alertDialog.show()
    }

    private fun requestSourcesFromAPI() {
        progressBar.isVisible = true
        val url = API_URL_SOURCES

        val getRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener{ response ->
                    sources = response.getJSONArray("sources")
                    sharedPref.getString("selectedSource", null)?.let { requestArticlesFromAPI(it) }
                    progressBar.isVisible = false
                },
                Response.ErrorListener { error-> showAlertDialog("Missing sources : $error") }
            ) {
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> =
                        HashMap()
                    params["User-Agent"] = "Mozilla/5.0"
                    return params
                }
            }
        queue.add(getRequest)
    }


    private fun requestArticlesFromAPI(source: String?) {
        progressBar.isVisible = true
        articlesData = ArrayList<Article>()

        selectedSource = source ?: sources.getJSONObject(0).getString("id")

        val url = "$API_URL&sources=$selectedSource"

        val getRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    parseArticles(response.getJSONArray("articles")) // doit rendre articleData
                    progressBar.isVisible = false
                },
                Response.ErrorListener { error -> showAlertDialog("Missing articles : $error") }
            ) {
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> =
                        HashMap()
                    params["User-Agent"] = "Mozilla/5.0"
                    return params
                }
            }
        queue.add(getRequest)
    }

    private fun parseArticles(articles: JSONArray) {
        for (i in 0 until articles.length()) {
            val article = articles.getJSONObject(i)
            val articlePreview = Article(article)
            articlesData.add(articlePreview)

        articlesLiveData.value = articlesData
        }
    }

    override fun onArticleClick(position: Int) {
        val article = articlesData[position]
        val intent = Intent(this, ArticleDetailActivity::class.java)

        intent.putExtra("title", article.title)
        intent.putExtra("author", article.author)
        intent.putExtra("date", article.date)
        intent.putExtra("sourceName", article.sourceName)
        intent.putExtra("description", article.description)
        intent.putExtra("link", article.link)
        intent.putExtra("url", article.urlToImage)

        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        val editor = sharedPref.edit()
        editor.putString("selectedSource", selectedSource)
        editor.apply()
    }
}