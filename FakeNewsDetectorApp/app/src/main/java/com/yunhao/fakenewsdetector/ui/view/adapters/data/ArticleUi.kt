package com.yunhao.fakenewsdetector.ui.view.adapters.data

data class ArticleUi(
    val id: Int,
    val title: String,
    val description: String?,
    val urlImage: String?,
    val url: String?,
    val publishedAt: String?,
    val isFavorite: Boolean = false,
    val predictionResult: String?,
    val isPredicting: Boolean = false,
)
