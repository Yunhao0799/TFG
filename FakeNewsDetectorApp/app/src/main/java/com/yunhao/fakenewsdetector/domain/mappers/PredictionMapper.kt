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
    val articles = listOfNotNull(this.articles, this.favorites)
        .flatten()
        .map { article ->
            Article(
                id = article.id,
                title = article.title,
                description = article.description,
                imageUrl = article.urlToImage,
                url = article.url,
                publishedAt = article.publishedAt,
                isFavorite = article.isFavorite,
                prediction = article.prediction
            )
        }
    return GetNewsResult(articles)
}