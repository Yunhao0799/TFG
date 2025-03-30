package com.yunhao.fakenewsdetector.domain.services

import com.yunhao.fakenewsdetector.domain.model.PredictionResult

interface IPredictionService {
    suspend fun predict(string: String): PredictionResult?
}