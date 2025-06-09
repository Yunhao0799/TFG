package com.yunhao.fakenewsdetector.data.network

import com.yunhao.fakenewsdetector.data.model.CreateUserDTO
import com.yunhao.fakenewsdetector.data.model.FavoriteToggleDTO
import com.yunhao.fakenewsdetector.data.model.FavoriteToggleResponseDTO
import com.yunhao.fakenewsdetector.data.model.LoginResponseDTO
import com.yunhao.fakenewsdetector.data.model.LoginUserDTO
import com.yunhao.fakenewsdetector.data.model.NewsResponseDTO
import com.yunhao.fakenewsdetector.data.model.PredictionDTO
import com.yunhao.fakenewsdetector.data.model.PredictionResponseDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Query

interface ApiInterface {
    @GET("backend/getCsrf/")
    fun getCsrf() : Call<Void>

    @POST("api/users/")
    suspend fun createUser(@Body userInfo: CreateUserDTO) : Response<CreateUserDTO>

    @POST("api/login/")
    suspend fun login(@Body userInfo: LoginUserDTO) : Response<LoginResponseDTO>

    @POST("api/predict/")
    suspend fun predict(@Body predictionDTO: PredictionDTO) : Response<PredictionResponseDTO>

    @GET("api/news/")
    suspend fun getNews(@Query("q") query: String? = null,
                        @Query("lang") language: String? = null,
                        @Query("country") country: String? = null,
                        @Query("category") category: String? = null,
                        @Query("endpoint") endpoint: String? = null) : Response<NewsResponseDTO>

    @GET("api/news/favorites")
    suspend fun getFavorites() : Response<NewsResponseDTO>

    @POST("api/news/favorite-toggle/")
    suspend fun toggleFavorite(@Body request: FavoriteToggleDTO): Response<FavoriteToggleResponseDTO>
}