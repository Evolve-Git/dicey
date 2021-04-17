package com.evolve.dicey.logic

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.*

fun setLocale(context: Context): Context {

    val pref: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    val savedLocale = Locale(pref.getString("lang", "en").toString())
    Locale.setDefault(savedLocale)
    val config = Configuration()
    config.setLocale(savedLocale)

    return context.createConfigurationContext(config)
}