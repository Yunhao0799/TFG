package com.yunhao.fakenewsdetector.domain.model

data class GetNewsResult (
    val articles: List<Article>
)

data class Article (
    val title: String,
    val description: String?,
    val urlToString: String?,
    val url: String,
    val publishedAt: String
)