package com.yunhao.fakenewsdetector.domain.model

data class GetNewsResult (
    val articles: List<Article>
)

data class Article (
    val id: Int,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val url: String,
    val publishedAt: String?,
    val isFavorite: Boolean = false,
    val prediction: String? = null,
)