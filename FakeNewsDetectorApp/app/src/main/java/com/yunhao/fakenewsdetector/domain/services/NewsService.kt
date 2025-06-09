package com.yunhao.fakenewsdetector.domain.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.yunhao.fakenewsdetector.data.model.FavoriteToggleDTO
import com.yunhao.fakenewsdetector.data.network.ApiClient
import com.yunhao.fakenewsdetector.domain.mappers.toDomain
import com.yunhao.fakenewsdetector.domain.model.GetNewsResult
import timber.log.Timber
import javax.inject.Inject

class NewsService @Inject constructor() : Service(), INewsService {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override suspend fun getNews(
        query: String?,
        language: String?,
        country: String?,
        category: String?,
        endpoint: String?
    ): GetNewsResult? {
        return try {
            val response = ApiClient.instance.getNews(query, language, country, category, endpoint)
            if (response.isSuccessful) {
                response.body()?.toDomain()
            } else {
                Timber.e("Failed to get the latest news: ${response.errorBody()}")
                null
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Unknown error"
            Timber.e("Failed to get the latest news: $errorMessage")
            null
        }
    }

    override suspend fun getFavorites(): GetNewsResult? {
        return try {
            val response = ApiClient.instance.getFavorites()
            if (response.isSuccessful) {
                response.body()?.toDomain()
            } else {
                Timber.e("Failed to get the favorites: ${response.errorBody()}")
                null
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Unknown error"
            Timber.e("Failed to get the favorites: $errorMessage")
            null
        }
    }

    override suspend fun toggleFavorite(articleId: Int, isFavorite: Boolean): Boolean {
        return try {
            val response = ApiClient.instance.toggleFavorite(FavoriteToggleDTO(articleId, isFavorite))
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}