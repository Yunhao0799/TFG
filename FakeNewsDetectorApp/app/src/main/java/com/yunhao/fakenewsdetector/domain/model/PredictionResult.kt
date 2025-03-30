package com.yunhao.fakenewsdetector.domain.model

data class PredictionResult (
    val isFake: Boolean,
    val isRealConfidence: Double,
    val isFakeConfidence: Double,
)