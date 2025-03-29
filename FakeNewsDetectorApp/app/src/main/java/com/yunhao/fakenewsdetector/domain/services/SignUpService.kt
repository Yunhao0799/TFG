package com.yunhao.fakenewsdetector.domain.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.gson.Gson
import com.yunhao.fakenewsdetector.data.model.CreateUserDTO
import com.yunhao.fakenewsdetector.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class SignUpService @Inject constructor() : Service(), ISignUpService {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(
        name: String,
        lastname: String,
        birthdate: String,
        email: String,
        password: String
    ): Pair<Boolean, String> {
        return try {
            val response = ApiClient.instance.createUser(
                CreateUserDTO(name, lastname, birthdate, email, password)
            )

            if (response.isSuccessful) {
                Timber.d("Sign-up successful")
                true to ""  // Success case (no error message needed)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Timber.e("Sign-up failed: $errorMessage")
                val errorJson = Gson().fromJson(errorMessage, Map::class.java)
                val error = (errorJson["email"] as List<*>)?.firstOrNull()?.toString() ?: ""
                false to error
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Unknown error"
            Timber.e("Sign-up error: $errorMessage")
            false to errorMessage
        }
    }

}