package com.yunhao.fakenewsdetector.domain.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.yunhao.fakenewsdetector.data.model.User
import com.yunhao.fakenewsdetector.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        ApiClient.instance
            .createUser(User("test", "pass"))
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                    } else {
                        // Handle failure
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    // Handle error
                }
            })

        return true
    }
}