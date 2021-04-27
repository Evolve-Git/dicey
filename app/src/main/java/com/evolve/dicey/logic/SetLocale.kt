package com.evolve.dicey.logic

import android.content.Context
import android.content.res.Configuration
import java.util.*

fun setLocale(context: Context): Context {

    val pref = Prefs(context)
    pref.versionCheck()

    val savedLocale = Locale(pref.lang)
    Locale.setDefault(savedLocale)
    val config = Configuration()
    config.setLocale(savedLocale)

    return context.createConfigurationContext(config)
}