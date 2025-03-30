package com.yunhao.fakenewsdetector.data.model

data class PredictionResponseDTO(
    val prediction: String,
    val realConfidence: Double,
    val fakeConfidence: Double
)