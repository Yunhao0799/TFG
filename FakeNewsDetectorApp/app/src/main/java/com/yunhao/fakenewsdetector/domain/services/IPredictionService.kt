package com.yunhao.fakenewsdetector.domain.services

import com.yunhao.fakenewsdetector.domain.model.PredictionResult

interface IPredictionService {
    suspend fun predict(string: String, articleId: Int? = null): PredictionResult?

    fun predictionToString(response: PredictionResult): String
}