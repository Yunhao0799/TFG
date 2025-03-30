package com.yunhao.fakenewsdetector.domain.mappers

import com.yunhao.fakenewsdetector.data.model.NewsResponseDTO
import com.yunhao.fakenewsdetector.data.model.PredictionResponseDTO
import com.yunhao.fakenewsdetector.domain.model.Article
import com.yunhao.fakenewsdetector.domain.model.GetNewsResult
import com.yunhao.fakenewsdetector.domain.model.PredictionResult

fun PredictionResponseDTO.toDomain(): PredictionResult {
    return PredictionResult(
        isFake = prediction == "fake",
        isRealConfidence = realConfidence,
        isFakeConfidence = fakeConfidence,
    )
}

fun NewsResponseDTO.toDomain(): GetNewsResult {
    val articles = mutableListOf<Article>()
    for (article in data.articles) {
        articles.add(
            Article(
                article.title,
                article.description,
                article.publishedAt
            )
        )
    }

    return GetNewsResult(articles)
}