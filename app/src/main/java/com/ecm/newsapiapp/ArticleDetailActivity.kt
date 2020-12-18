package com.ecm.newsapiapp

import android.content.Intent
import com.ecm.newsapiapp.R
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class ArticleDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)
        setSupportActionBar(findViewById(R.id.toolbar))

        val i = intent

        val title: String? = i.getStringExtra("title")
        val author: String? = i.getStringExtra("author")
        val date: String? = i.getStringExtra("date")
        val source: String? = i.getStringExtra("sourceName")
        val description: String? = i.getStringExtra("description")
        val link: String? = i.getStringExtra("link")
        val url: String? = i.getStringExtra("url")

        val titleView = findViewById<TextView>(R.id.title) as TextView
        val authorView = findViewById<TextView>(R.id.author) as TextView
        val dateView = findViewById<TextView>(R.id.date) as TextView
        val sourceView = findViewById<TextView>(R.id.source) as TextView
        val descView = findViewById<TextView>(R.id.description) as TextView
        val imgView = findViewById<ImageView>(R.id.bigPicture) as ImageView

        titleView.text = title
        authorView.text = author
        dateView.text = date
        sourceView.text = source
        descView.text = description
        if (url != null) {
            Picasso.get().load(url).into(imgView)
        } else {
            imgView.setImageResource(R.drawable.default_article_img_resized)
        }

        this.findViewById<FloatingActionButton>(R.id.home).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        this.findViewById<Button>(R.id.button_first).setOnClickListener {
            startActivity(Intent(this, ReadOnlineActivity::class.java).apply {
                putExtra("link", link)
            })
        }



//          Tentative de fragment abandonnée
//        if (savedInstanceState == null) {
//            var fragment: FirstFragment = FirstFragment()
//            fragment.arguments = intent.extras
//            supportFragmentManager.beginTransaction()
//                .add(R.layout.fragment_first, fragment, false)
//                .commit()

//        }
    }
}