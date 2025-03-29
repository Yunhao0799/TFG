package com.yunhao.fakenewsdetector.ui.utils

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StringProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun get(@StringRes resId: Int): String =
        context.getString(resId)

    fun get(@StringRes resId: Int, vararg args: Any): String =
        context.getString(resId, *args)
}