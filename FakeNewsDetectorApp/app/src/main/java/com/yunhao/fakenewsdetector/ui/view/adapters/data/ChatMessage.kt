package com.yunhao.fakenewsdetector.ui.view.adapters.data

import java.util.SimpleTimeZone

data class ChatMessage (
    val message: String,
    val isSentByUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)