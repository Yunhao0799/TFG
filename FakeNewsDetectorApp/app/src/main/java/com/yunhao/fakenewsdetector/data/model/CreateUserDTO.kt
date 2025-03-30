package com.yunhao.fakenewsdetector.data.model

import com.google.gson.annotations.SerializedName

data class CreateUserDTO(
    @SerializedName("first_name")
    val firstname: String,
    @SerializedName("last_name")
    val lastname: String,
    val birthdate: String,
    val email: String,
    val password: String,
)
