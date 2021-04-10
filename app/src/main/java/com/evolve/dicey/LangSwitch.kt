package com.evolve.dicey

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.*

fun setLocale(context: Context): Context {

    val pref: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    //Credits to Idan Atsmon
    val savedLocale = Locale(pref.getString("lang", "en").toString()) // else return the original untouched context

    // as part of creating a new context that contains the new locale we also need to override the default locale.
    Locale.setDefault(savedLocale)

    // create new configuration with the saved locale
    val config = Configuration()
    config.setLocale(savedLocale)

    return context.createConfigurationContext(config)
}