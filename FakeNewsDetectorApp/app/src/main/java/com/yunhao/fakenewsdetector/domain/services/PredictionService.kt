package com.yunhao.fakenewsdetector.domain.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.yunhao.fakenewsdetector.data.model.PredictionDTO
import com.yunhao.fakenewsdetector.data.network.ApiClient
import com.yunhao.fakenewsdetector.domain.mappers.toDomain
import com.yunhao.fakenewsdetector.domain.model.PredictionResult
import timber.log.Timber
import javax.inject.Inject

class PredictionService @Inject constructor() : Service(), IPredictionService {
    override suspend fun predict(string: String, articleId: Int?): PredictionResult? {
        return try {
            val response = ApiClient.instance.predict(PredictionDTO(string, articleId))
            if (response.isSuccessful) {
                response.body()?.toDomain()
            } else {
                Timber.e("Prediction error: ${response.errorBody()}")
                null
            }

        } catch (e: Exception) {
            Timber.e("Prediction error: ${e.message}")
            null
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}