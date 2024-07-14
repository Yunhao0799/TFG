package com.yunhao.fakenewsdetector.utils

import android.util.Log
import timber.log.Timber

class ReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        Log.println(priority, tag, message)

        // Log the exception
        if (t != null) {
            Log.println(Log.ERROR, tag, t.stackTraceToString())

            if (priority == Log.ERROR) {

            } else if (priority == Log.WARN) {

            }
        }
    }
}