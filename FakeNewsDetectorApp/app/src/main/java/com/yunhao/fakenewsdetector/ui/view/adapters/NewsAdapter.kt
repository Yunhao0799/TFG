package com.yunhao.fakenewsdetector.ui.view.adapters

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
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
import com.yunhao.fakenewsdetector.ui.view.adapters.data.ArticleUi
import com.yunhao.fakenewsdetector.utils.DateTimeHelper
import timber.log.Timber
import java.time.Instant

class NewsAdapter (
    private val onGoToSourceCallback: (ArticleUi) -> Unit,
    private val onLikeCallback: (ArticleUi) -> Unit,
    private val onPredictionCallback: (ArticleUi) -> Unit,
) : ListAdapter<ArticleUi, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_discover_news, parent, false)
        return NewsViewHolder(view, onGoToSourceCallback, onLikeCallback, onPredictionCallback)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val newsViewHolder = holder as NewsViewHolder
        val article = getItem(position)
        newsViewHolder.setUpListeners(article)
        newsViewHolder.bind(article)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        }
        else {
            val article = getItem(position)
            val payload = payloads[0] as Bundle
            (holder as NewsViewHolder).bindPartial(article, payload)
        }
    }

    class NewsViewHolder(
        itemView: View,
        private val onGoToSourceCallback: (ArticleUi) -> Unit,
        private val onLikeCallback: (ArticleUi) -> Unit,
        private val onPredictionCallback: (ArticleUi) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val imageView: ShapeableImageView?
        private val title: TextView?
        private val description: TextView?
        private val datetime: TextView?
        private val predictionResult: TextView?
        private val favoriteButton: CheckBox?
        private val predictionButton: Button?
        private val goToSourceButton: Button?
        private val predictionResultLayout: LinearLayout?

        init {
            imageView = itemView.findViewById(R.id.image)
            title = itemView.findViewById(R.id.title)
            description = itemView.findViewById(R.id.description)
            datetime = itemView.findViewById(R.id.datetime)
            predictionResult = itemView.findViewById(R.id.predictionResult)
            favoriteButton = itemView.findViewById(R.id.favoriteButton)
            predictionButton = itemView.findViewById(R.id.predictionButton)
            goToSourceButton = itemView.findViewById(R.id.goToSourceButton)
            predictionResultLayout = itemView.findViewById(R.id.predictionResultLayout)
        }

        fun bind(article: ArticleUi) {
            imageView?.visibility = View.VISIBLE

            title?.text = article.title
            description?.text = article.description
            datetime?.text = DateTimeHelper.getFormattedDateTime(
                Instant.parse(article.publishedAt).toEpochMilli(), pattern = "dd/MM/yyyy HH:mm")

            imageView?.let {
                Glide.with(itemView.context)
                    .load(article.urlImage)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            imageView?.visibility = View.GONE
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

            updatePrediction(article)
        }

        fun setUpListeners(article: ArticleUi) {
            favoriteButton?.setOnCheckedChangeListener { buttonView, isChecked ->
                onLikeCallback(article)
            }

            goToSourceButton?.setOnClickListener {
                onGoToSourceCallback(article)
            }

            predictionButton?.setOnClickListener {
                predictionButton.isEnabled = false

                onPredictionCallback(article.copy(isPredicting = true))
                Timber.d("EndOnClick")
            }
        }

        fun bindPartial(article: ArticleUi, payload: Bundle) {
            if (payload.containsKey("KEY_RESULT")) {
                updatePrediction(article)
            }
        }

        private fun updatePrediction(article: ArticleUi) {
            predictionResult?.text = article.predictionResult
            predictionButton?.isEnabled = !article.isPredicting
            predictionButton?.visibility = if (predictionResult?.text.isNullOrBlank()) View.VISIBLE else View.INVISIBLE
            predictionResultLayout?.visibility = if (predictionResult?.text.isNullOrBlank()) View.GONE else View.VISIBLE
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ArticleUi>() {
        override fun areItemsTheSame(oldItem: ArticleUi, newItem: ArticleUi): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: ArticleUi, newItem: ArticleUi): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: ArticleUi, newItem: ArticleUi): Any? {
            val bundle = Bundle()
            if (oldItem.predictionResult != newItem.predictionResult) {
                bundle.putString("KEY_RESULT", newItem.predictionResult)
            }
            return if (bundle.isEmpty) null else bundle
        }
    }
}