package com.yunhao.fakenewsdetector.domain.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
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
        return true
    }
}