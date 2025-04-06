package com.yunhao.fakenewsdetector.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateTimeHelper {
    fun getFormattedDateTime(timestamp: Long, timeZoneId: ZoneId = ZoneId.systemDefault(), pattern: String = "dd/MM/yyyy"): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return Instant.ofEpochMilli(timestamp)
            .atZone(timeZoneId)
            .toLocalDateTime()
            .format(formatter)
    }
}