package com.yunhao.fakenewsdetector.domain.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
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
    ): Boolean {
        return try {
            val response = ApiClient.instance.createUser(CreateUserDTO(name, lastname, birthdate, email, password))

            if (response.isSuccessful) {
                Timber.d("Sign-up successful")
                true  // Return success
            } else {
                Timber.e("Sign-up failed: ${response.errorBody()?.string()}")
                false // Return failure
            }
        } catch (e: Exception) {
            Timber.e("Sign-up error: ${e.message}")
            false
        }
    }

}