package com.yunhao.fakenewsdetector.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class PreferencesManager {

    enum class Properties {
        CSRF_TOKEN
    }

    var preferences: SharedPreferences? = null
        private set
    var editor: SharedPreferences.Editor? = null
        private set

    companion object {
        /**
         * You need to call initializeFrom(context:) first, or use with(context:) static method.
         */
        val default = PreferencesManager()
        val custom = PreferencesManager()
        private const val customName = "AumenturPreferences"

        fun initializeFrom(context: Context) {
            initializeCustomFrom(context)
            initializeDefaultFrom(context)
        }

        fun customWith(context: Context): PreferencesManager {
            if(custom.preferences == null || custom.editor == null) {
                initializeDefaultFrom(context)
            }
            return custom
        }

        fun with(context: Context): PreferencesManager {
            if(default.preferences == null || default.editor == null) {
                initializeCustomFrom(context)
            }
            return default
        }

        private fun initializeCustomFrom(context: Context) {
            custom.preferences = context.getSharedPreferences(customName, Context.MODE_PRIVATE)
            custom.editor = custom.preferences?.edit().also { it?.apply() }
            custom.initDefaultValues()
        }
        private fun initializeDefaultFrom(context: Context) {
            default.preferences = PreferenceManager.getDefaultSharedPreferences(context)
            default.editor = default.preferences?.edit().also { it?.apply() }
        }
    }

    inline operator fun <reified T>get(property: Properties, default: T): T {

        if(!this.exists(property.name)) { return default }

        return when (default) {
            is Int -> this.preferences?.getInt(property.name, default) as T
            is Long -> this.preferences?.getLong(property.name, default) as T
            is Float -> this.preferences?.getFloat(property.name, default) as T
            is Boolean -> this.preferences?.getBoolean(property.name, default) as T
            is String -> this.preferences?.getString(property.name, default) as T
            else -> default
        }
    }

    operator fun <T>set(property: Properties, value: T) {

        when (value) {
            is Int -> this.editor?.putInt(property.name, value)
            is Long -> this.editor?.putLong(property.name, value)
            is Float -> this.editor?.putFloat(property.name, value)
            is Boolean -> this.editor?.putBoolean(property.name, value)
            is String -> this.editor?.putString(property.name, value)
            else -> return
        }

        this.editor?.apply()
    }

    fun exists(propertyKey: String): Boolean {
        return this.preferences?.contains(propertyKey) == true
    }

    fun apply(vararg values: Pair<Any, Properties>) {
        values.forEach { this[it.second] = it.first }
        this.editor?.apply()
    }

    fun remove(property: Properties): PreferencesManager {
        this.editor?.remove(property.name)
        this.editor?.apply()
        return this
    }

    fun clear(): PreferencesManager {
        this.editor?.clear()
        this.editor?.apply()
        return this
    }

    private fun initDefaultValues() {
    }
}