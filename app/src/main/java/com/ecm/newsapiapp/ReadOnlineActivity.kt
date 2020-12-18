package com.ecm.newsapiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.core.view.isVisible

class ReadOnlineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_online)

        val webView = findViewById<WebView>(R.id.reader)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.isVisible = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, weburl: String) {
                progressBar.isVisible = false
            }
        }

        intent.getStringExtra("link")?.let { webView.loadUrl(it) }

    }
}