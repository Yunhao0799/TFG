package com.yunhao.fakenewsdetector.data.model

import com.google.gson.annotations.SerializedName

data class NewsResponseDTO(
    val articles: List<Article>?,
    val favorites: List<Article>?,
)

data class Article(
    val id: Int,
    val url: String,
    val title: String,
    val author: String?,
    val source: ArticleSource?,
    val content: String?,
    val urlToImage: String?,
    val description: String?,
    @SerializedName("published_at")
    val publishedAt: String?,
    @SerializedName("is_favorite")
    val isFavorite: Boolean = false,
    val prediction: String? = null,
)

data class ArticleSource(
    val id: String?,
    val name: String
)