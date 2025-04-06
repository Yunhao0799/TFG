package com.yunhao.fakenewsdetector.ui.view.adapters

import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.imageview.ShapeableImageView
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.data.model.NewsData
import com.yunhao.fakenewsdetector.ui.view.adapters.data.ArticleUi
import com.yunhao.fakenewsdetector.utils.DateTimeHelper
import timber.log.Timber
import java.time.Instant

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
            itemView.findViewById<TextView>(R.id.datetime).text = DateTimeHelper.getFormattedDateTime(
                Instant.parse(article.publishedAt).toEpochMilli(), pattern = "dd/MM/yyyy HH:mm")

            val imageView = itemView.findViewById<ShapeableImageView>(R.id.image)

            Glide.with(itemView.context)
                .load(article.urlImage)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageView.visibility = GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(imageView)
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