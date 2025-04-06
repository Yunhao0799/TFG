package com.yunhao.fakenewsdetector.ui.view.adapters.data

import com.yunhao.fakenewsdetector.domain.model.PredictionResult

data class ArticleUi(
    val title: String,
    val description: String?,
    val urlImage: String?,
    val url: String,
    val publishedAt: String,
    val predictionResult: String?,
    val isPredicting: Boolean = false,
)
