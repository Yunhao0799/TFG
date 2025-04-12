package com.yunhao.fakenewsdetector.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object LinkHelper {
    fun Context.openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(intent)
    }
}