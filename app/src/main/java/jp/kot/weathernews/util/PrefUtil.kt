package jp.kot.weathernews.util

import android.content.Context
import android.preference.PreferenceManager

object PrefUtil {

    fun remove(context: Context, key: String)
            = PreferenceManager.getDefaultSharedPreferences(context).edit().remove(key).commit()

    fun getString(context: Context, key: String): String
            = getString(context, key, "")

    fun getString(context: Context, key: String, default: String): String
            = PreferenceManager.getDefaultSharedPreferences(context).getString(key, default)

    fun putString(context: Context, key: String, value: String)
            = PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).commit()

    fun getBoolean(context: Context, key: String): Boolean
            = getBoolean(context, key, false)

    fun getBoolean(context: Context, key: String, default: Boolean): Boolean
            = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, default)

    fun putBoolean(context: Context, key: String, value: Boolean)
            = PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, value).commit()

    fun getInt(context: Context, key: String): Int
            = getInt(context, key, 0)

    fun getInt(context: Context, key: String, default: Int): Int
            = PreferenceManager.getDefaultSharedPreferences(context).getInt(key, default)

    fun putInt(context: Context, key: String, value: Int)
            = PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).commit()

    fun getLong(context: Context, key: String): Long
            = getLong(context, key, 0)

    fun getLong(context: Context, key: String, default: Long): Long
            = PreferenceManager.getDefaultSharedPreferences(context).getLong(key, default)

    fun putLong(context: Context, key: String, value: Long)
            = PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(key, value).commit()

    fun getFloat(context: Context, key: String): Float
            = getFloat(context, key, 0f)

    fun getFloat(context: Context, key: String, default: Float): Float
            = PreferenceManager.getDefaultSharedPreferences(context).getFloat(key, default)

    fun putFloat(context: Context, key: String, value: Float)
            = PreferenceManager.getDefaultSharedPreferences(context).edit().putFloat(key, value).commit()
}