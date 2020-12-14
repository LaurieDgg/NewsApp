package com.ecm.newsapiapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView



class RecyclerAdapter (private val onClick: (Article) -> Unit) :
    ListAdapter<Article, RecyclerAdapter.ArticleViewHolder>(ArticleDiffCallback) {

    class ArticleViewHolder(itemView: View, val onClick: (Article) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val itemTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val itemDetail: TextView = itemView.findViewById(R.id.tv_description)
        private val itemDate: TextView = itemView.findViewById(R.id.tv_date)
        private val itemPicture: ImageView = itemView.findViewById(R.id.iv_image)
        private var currentArticle: Article? = null


        init {
            itemView.setOnClickListener {
                currentArticle?.let {
                    onClick(it)
                }
            }
        }

        fun bind(article: Article) {
            currentArticle = article

            itemTitle.text = article.title
            itemDetail.text = article.description
            itemDate.text = article.date
            if (article.urlToImage != null) {
                itemPicture.setImageResource(R.drawable.default_article_img)
            } else {
                itemPicture.setImageResource(R.drawable.default_article_img)
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
        holder.bind(article)
    }
}

//    override fun getItemCount(): Int {
//        return titles.size
//    }

object ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.title == newItem.title
    }
}