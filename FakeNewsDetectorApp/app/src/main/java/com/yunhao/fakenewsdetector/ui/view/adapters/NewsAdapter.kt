package com.yunhao.fakenewsdetector.ui.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.data.model.NewsData
import com.yunhao.fakenewsdetector.ui.view.adapters.data.ArticleUi
import com.yunhao.fakenewsdetector.utils.DateTimeHelper

class NewsAdapter : ListAdapter<ArticleUi, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_discover_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsViewHolder)?.bind((getItem(position)))
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(article: ArticleUi) {
            itemView.findViewById<TextView>(R.id.title).text = article.title
            itemView.findViewById<TextView>(R.id.description).text = article.description
            itemView.findViewById<TextView>(R.id.datetime).text = article.publishedAt
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ArticleUi>() {
        override fun areItemsTheSame(oldItem: ArticleUi, newItem: ArticleUi): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ArticleUi, newItem: ArticleUi): Boolean {
            return oldItem == newItem
        }
    }
}