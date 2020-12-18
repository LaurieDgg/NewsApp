package com.ecm.newsapiapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class RecyclerAdapter (private val articles: ArrayList<Article>,
                       private val mOnArticleListener: OnArticleListener) :

    ListAdapter<Article, RecyclerAdapter.ArticleViewHolder>(ArticleDiffCallback) {

    class ArticleViewHolder(itemView: View, onArticleListener: OnArticleListener): RecyclerView.ViewHolder(itemView) {

        val itemTitle: TextView = itemView.findViewById(R.id.tv_title)
        val itemAuthor: TextView = itemView.findViewById(R.id.tv_author)
        val itemDate: TextView = itemView.findViewById(R.id.tv_date)
        val itemPicture: ImageView = itemView.findViewById(R.id.iv_image)

        init {
            itemView.setOnClickListener {
                onArticleListener.onArticleClick(adapterPosition)
            }
        }

    }

    interface OnArticleListener{
        fun onArticleClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return if (viewType == 0) {
            ArticleViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.item_layout_0, parent, false), mOnArticleListener)
        } else {
            ArticleViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.item_layout_1, parent, false), mOnArticleListener)
        }
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
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

    override fun getItemViewType(position: Int): Int {
        return position % 2
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