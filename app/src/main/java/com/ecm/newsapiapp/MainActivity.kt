package com.ecm.newsapiapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var dateList = mutableListOf<String>()
    private var imagesList = mutableListOf<Int>()

    val LANG = "fr"
//    val API_KEY = "ee05a54c472449fd9afb26a8f0756a88"
    val API_KEY = "d31f5fa5f03443dd8a1b9e3fde92ec34"
    val API_URL_SOURCES = "https://newsapi.org/v2/sources?apiKey=$API_KEY&language=$LANG"
    val API_URL = "https://newsapi.org/v2/everything?apiKey=$API_KEY&language=$LANG"

    var sources = JSONArray()
    var selectedSource = ""

    lateinit var queue: RequestQueue
    lateinit var progressBar: ProgressBar
    lateinit var textView: TextView

    var articlesData = ArrayList<Article>()

    var articlesLiveData = MutableLiveData<ArrayList<Article>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        queue = Volley.newRequestQueue(this)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        textView = findViewById<TextView>(R.id.texty)

        requestSourcesFromAPI()

        val recyclerAdapter = RecyclerAdapter { article -> onClick(article) }
        val recyclerView: RecyclerView = findViewById(R.id.rv_recyclerView)

        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        articlesLiveData.observe(this, Observer { it?.let {
            recyclerAdapter.submitList(it.toMutableList())
        } })
    }

    private fun onClick(article: Article) {
        val intent = Intent(this, ArticleDetailActivity()::class.java)
        startActivity(intent)
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


    private fun requestSourcesFromAPI() {
        progressBar.isVisible = true
        val url = API_URL_SOURCES
        val textView = findViewById<TextView>(R.id.texty)

        val getRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener{ response ->
                    sources = response.getJSONArray("sources")
                    progressBar.isVisible = false
                },
                Response.ErrorListener { error -> textView.text = "No sources, error: %s".format(error.toString()) }
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


    private fun requestArticlesFromAPI(selectedSource: String) {
        progressBar.isVisible = true
        articlesData = ArrayList<Article>()

        val url = "$API_URL&sources=$selectedSource"
        val textView = findViewById<TextView>(R.id.texty)

        val getRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    parseArticles(response.getJSONArray("articles")) // doit rendre articleData
                    progressBar.isVisible = false
                },
                Response.ErrorListener { error -> textView.text = "articles error: %s".format(error.toString()) }
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
}