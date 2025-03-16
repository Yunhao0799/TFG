package com.yunhao.fakenewsdetector.domain.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.yunhao.fakenewsdetector.data.model.CreateUserDTO
import com.yunhao.fakenewsdetector.data.model.LoginUserDTO
import com.yunhao.fakenewsdetector.data.network.ApiClient
import com.yunhao.fakenewsdetector.utils.PreferencesManager
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class LoginService @Inject constructor() : Service(), ILoginService {


    override suspend fun login(username: String, password: String): Boolean {
        return try {
            val response = ApiClient.instance.login(LoginUserDTO(username, password))

            if (response.isSuccessful) {
                Timber.d("Login successful")
                PreferencesManager.default[PreferencesManager.Properties.TOKEN] = response.body()?.token ?: ""
                true  // Return success
            } else {
                Timber.e("Login failed: ${response.errorBody()?.string()}")
                PreferencesManager.default[PreferencesManager.Properties.TOKEN] = ""
                false // Return failure
            }
        } catch (e: Exception) {
            Timber.e("Login error: ${e.message}")
            PreferencesManager.default[PreferencesManager.Properties.TOKEN] = ""
            false
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}