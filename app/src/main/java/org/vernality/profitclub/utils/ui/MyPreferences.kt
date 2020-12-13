package org.vernality.profitclub.utils.ui

import android.content.Context
import android.content.SharedPreferences
import org.koin.core.KoinComponent
import org.koin.core.inject

public const val APP_PREF_REG_STATUS = "registrationStatus"

class MyPreferences(private var context: Context): KoinComponent {

    val APP_PREFERENCES = "mysettings"



    lateinit var preferences: SharedPreferences


    fun getMyPreferences() = this


    init {
        preferences =
            context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        println("create MyPref()")
    }


    fun setIntPref(key: String, units: Int) {
        preferences.edit().putInt(key, units).apply()
        println("set in pref, key =$key, units =$units")
    }

    fun getIntPref(key: String): Int? {
        println("get in pref, key =" + key + ", value =" + preferences.getInt(key, 0))
        return preferences.getInt(key, 0)
    }

    fun setStrPref(key: String?, units: String?) {
        preferences.edit().putString(key, units).apply()
    }

    fun getStrPref(key: String?): String? {
        return preferences.getString(key, "")
    }

    fun setBooleanPref(key: String?, units: Boolean) {
        preferences.edit().putBoolean(key, units).apply()
    }

    fun getBooleanPref(key: String?): Boolean {
        return preferences.getBoolean(key, false)
    }


}