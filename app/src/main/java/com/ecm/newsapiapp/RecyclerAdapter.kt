package com.ecm.newsapiapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.log
import com.squareup.picasso.Picasso


class RecyclerAdapter (private val onClick: (Article) -> Unit) :
    ListAdapter<Article, RecyclerAdapter.ArticleViewHolder>(ArticleDiffCallback) {

    class ArticleViewHolder(itemView: View, val onClick: (Article) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        val itemTitle: TextView = itemView.findViewById(R.id.tv_title)
        val itemAuthor: TextView = itemView.findViewById(R.id.tv_author)
        val itemDate: TextView = itemView.findViewById(R.id.tv_date)
        val itemPicture: ImageView = itemView.findViewById(R.id.iv_image)
        var TAG: String = "tag ! you're it!"
        var currentArticle: Article? = null


        init {
            itemView.setOnClickListener { v: View ->
                val position: Int = adapterPosition
                fun onClick(v: View) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return RecyclerAdapter.ArticleViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ArticleViewHolder, position: Int) {
        val article = getItem(position)

        holder.itemTitle.text = article.title
        holder.itemAuthor.text = article.author
        holder.itemDate.text = article.date
        if (article.urlToImage != null) {
            val urlImage= article.urlToImage
            Picasso.get().load(urlImage).into(holder.itemPicture)
        } else {
            holder.itemPicture.setImageResource(R.drawable.default_article_img_resized)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }
}

object ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.title == newItem.title
    }
}