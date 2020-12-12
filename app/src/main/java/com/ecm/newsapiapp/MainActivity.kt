package com.ecm.newsapiapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var dateList = mutableListOf<String>()
    private var imagesList = mutableListOf<Int>()

    val LANG = "fr"
//    val API_KEY = "ee05a54c472449fd9afb26a8f0756a88"
    val API_KEY = "d31f5fa5f03443dd8a1b9e3fde92ec34"
    val API_URL_SOURCES = "https://newsapi.org/v2/sources?apiKey=%s&language=%s".format(API_KEY, LANG)
    val API_URL = "https://newsapi.org/v2/everything?apiKey=%s&language=%s&sources=google-news-fr".format(API_KEY, LANG)

    var sources = JSONArray()

    lateinit var queue: RequestQueue
    lateinit var rv_recyclerView: RecyclerView
    lateinit var textView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        queue = Volley.newRequestQueue(this)

        requestEverythingFromAPI()
        requestSourcesFromAPI()
//        postToList()

        rv_recyclerView = findViewById<RecyclerView>(R.id.rv_recyclerView)

        rv_recyclerView.layoutManager = LinearLayoutManager(this)
        rv_recyclerView.adapter = RecyclerAdapter(titlesList, descList, dateList, imagesList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the main_menu; this adds items to the action bar if it is present.
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


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        super.onOptionsItemSelected(item)
//        currentSourceId = sources.getJSONObject(item.itemId).getString("id")
//        currentPage = 1
//        getArticles(currentSourceId, currentPage)
//        return true

    private fun addToList(title: String, description: String, date: String, image: Int) {
        titlesList.add(title)
        descList.add(description)
        dateList.add(date)
        imagesList.add(image)

    }

    private fun parseResponse(response: JSONObject) {
        val articleArray: JSONArray = response.getJSONArray("articles")
        for (i in 0 until articleArray.length()) {
            val article = articleArray.getJSONObject(i)
            val title = article.getString("title").take(50)
            val description = article.getString("author")
            val date = article.getString("publishedAt").substring(0, 10)
            addToList(title, description, date, R.mipmap.ic_launcher_round)
        }
    }

    private fun requestSourcesFromAPI() {
        val url = API_URL_SOURCES
        val textView = findViewById<TextView>(R.id.texty)

        val getRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    sources = response.getJSONArray("sources")
                },
                Response.ErrorListener { error -> textView.text = "No sources, error: %s".format(error.toString())
                }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> =
                        HashMap()
                    params["User-Agent"] = "Mozilla/5.0"
                    return params
                }
            }
        queue.add(getRequest)
    }

    private fun requestEverythingFromAPI() {
        val url = API_URL
        val textView = findViewById<TextView>(R.id.texty)

        val getRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
//                    textView.text = "Response is: ${response.toString().substring(0, 500)}"
                    parseResponse(response)
                },
                Response.ErrorListener { error -> textView.text = "ERROR: %s".format(error.toString())
                }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> =
                        HashMap()
                    params["User-Agent"] = "Mozilla/5.0"
                    return params
                }
            }
        queue.add(getRequest)
    }
}