package com.yunhao.fakenewsdetector.domain.model

data class GetNewsResult (
    val articles: List<Article>
)

data class Article (
    val title: String,
    val description: String?,
    val publishedAt: String
)