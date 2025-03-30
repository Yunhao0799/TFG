package com.yunhao.fakenewsdetector.domain.mappers

import com.yunhao.fakenewsdetector.data.model.PredictionResponseDTO
import com.yunhao.fakenewsdetector.domain.model.PredictionResult

fun PredictionResponseDTO.toDomain(): PredictionResult {
    return PredictionResult(
        isFake = prediction == "fake",
        isRealConfidence = realConfidence,
        isFakeConfidence = fakeConfidence,
    )
}