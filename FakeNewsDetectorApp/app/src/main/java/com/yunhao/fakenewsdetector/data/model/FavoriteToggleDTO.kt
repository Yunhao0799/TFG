package com.yunhao.fakenewsdetector.data.model

import com.google.gson.annotations.SerializedName

data class FavoriteToggleDTO(
    @SerializedName("article_id")
    val articleId: Int,
    val favorite: Boolean
)
