package com.yunhao.fakenewsdetector.data.model

data class NewsResponseDTO(
    val source: String,
    val data: NewsData
)

data class NewsData(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

data class Article(
    val url: String,
    val title: String,
    val author: String?,
    val source: ArticleSource?,
    val content: String?,
    val urlToImage: String?,
    val description: String?,
    val publishedAt: String
)

data class ArticleSource(
    val id: String?,
    val name: String
)