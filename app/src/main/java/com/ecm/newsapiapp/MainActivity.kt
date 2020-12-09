package com.ecm.newsapiapp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


class MainActivity : AppCompatActivity() {

    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imagesList = mutableListOf<Int>()

//    val API_KEY = "ee05a54c472449fd9afb26a8f0756a88"
    val API_KEY = "d31f5fa5f03443dd8a1b9e3fde92ec34"
    val API_URL = "https://newsapi.org/v2/everything?apiKey=" + API_KEY + "&language=fr&sources=google-news-fr"


    lateinit var rv_recyclerView: RecyclerView
    lateinit var textView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        postToList()

        requestEverythingFromAPI()

// ...

//// Instantiate the RequestQueue.
//        val queue = Volley.newRequestQueue(this)
////        val url = "https://www.google.com"
//        val url = API_URL
//
//// Request a string response from the provided URL.
//        val stringRequest = StringRequest(
//            Request.Method.GET, url,
//            Response.Listener<String> { response ->
//                // Display the first 500 characters of the response string.
//                textView.text = "Response is: ${response.substring(0, 500)}"
//            },
//            Response.ErrorListener { textView.text = "That didn't work!" })
//
//// Add the request to the RequestQueue.
//        queue.add(stringRequest)


        rv_recyclerView = findViewById<RecyclerView>(R.id.rv_recyclerView)

        rv_recyclerView.layoutManager = LinearLayoutManager(this)
        rv_recyclerView.adapter = RecyclerAdapter(titlesList, descList, imagesList)
    }

    private fun requestEverythingFromAPI() {

        val queue = Volley.newRequestQueue(this)
        val url = API_URL
        val textView = findViewById<TextView>(R.id.texty)

        val getRequest: StringRequest =
            object : StringRequest(
                Method.GET, url,
                Response.Listener<String> { response ->
//                Display the first 500 characters of the response string.
                textView.text = "Response is: ${response.substring(0, 500)}"
            },
                Response.ErrorListener { textView.text = "That didn't work!" }
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

    private fun addToList(title: String, description: String, image: Int) {
        titlesList.add(title)
        descList.add(description)
        imagesList.add(image)

    }

    private fun postToList() {
        for (i in 1..25) {
            addToList("Title $i", "description $i", R.mipmap.ic_launcher)
        }
    }


}