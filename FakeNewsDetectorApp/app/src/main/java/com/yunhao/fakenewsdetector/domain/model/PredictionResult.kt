package com.yunhao.fakenewsdetector.domain.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

data class PredictionResult (
    val isFake: Boolean,
    val isRealConfidence: Double,
    val isFakeConfidence: Double,
)

class PredictionResultDeserializer : JsonDeserializer<PredictionResult> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): PredictionResult {
        val obj = json.asJsonObject

        val prediction = obj.get("prediction").asString
        val realConfidence = obj.get("realConfidence").asDouble
        val fakeConfidence = obj.get("fakeConfidence").asDouble

        return PredictionResult(
            isFake = prediction.equals("fake", ignoreCase = true),
            isRealConfidence = realConfidence,
            isFakeConfidence = fakeConfidence
        )
    }
}