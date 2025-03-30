package com.yunhao.fakenewsdetector.data.network

import com.yunhao.fakenewsdetector.data.model.CreateUserDTO
import com.yunhao.fakenewsdetector.data.model.LoginResponseDTO
import com.yunhao.fakenewsdetector.data.model.LoginUserDTO
import com.yunhao.fakenewsdetector.data.model.PredictionDTO
import com.yunhao.fakenewsdetector.data.model.PredictionResponseDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

interface ApiInterface {
    @GET("backend/getCsrf/")
    fun getCsrf() : Call<Void>

    @POST("api/users/")
    suspend fun createUser(@Body userInfo: CreateUserDTO) : Response<CreateUserDTO>

    @POST("api/login/")
    suspend fun login(@Body userInfo: LoginUserDTO) : Response<LoginResponseDTO>

    @POST("api/predict/")
    suspend fun predict(@Body predictionDTO: PredictionDTO) : Response<PredictionResponseDTO>
}