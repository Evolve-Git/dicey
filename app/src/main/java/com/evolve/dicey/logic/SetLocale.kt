package com.evolve.dicey.logic

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

fun setLocale(context: Context): Context {

    val pref = Prefs(context)

    val savedLocale = Locale(pref.lang)
    Locale.setDefault(savedLocale)
    val config = Configuration()
    config.setLocale(savedLocale)

    return context.createConfigurationContext(config)
}