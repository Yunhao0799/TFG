package com.yunhao.fakenewsdetector.utils

import javax.inject.Singleton

@Singleton
object UserHelper {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}