package com.yunhao.fakenewsdetector.domain.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import javax.inject.Inject

class LoginService @Inject constructor() : Service(), ILoginService {


    override suspend fun login(username: String, password: String): Boolean {
        return true
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}