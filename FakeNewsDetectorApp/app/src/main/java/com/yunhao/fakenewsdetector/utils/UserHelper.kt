package com.yunhao.fakenewsdetector.utils

object UserHelper {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}