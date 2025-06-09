package com.yunhao.fakenewsdetector.data.model

import com.google.gson.annotations.SerializedName

data class PredictionDTO(
    @SerializedName("input_string")
    val string: String,
    val id: Int?
)
