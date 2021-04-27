package com.evolve.dicey.logic

import android.content.Context
import android.content.SharedPreferences
import com.evolve.dicey.BuildConfig
import com.evolve.dicey.R

class Prefs (context: Context){
    private val pref: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    //Can't make the getter use the context hence this stupid waste of space:
    private val temp1 = context.resources.getString(R.string.n1)
    private val temp2 = context.resources.getString(R.string.n2)
    var isTTSon: Boolean
        get() = pref.getBoolean("tts", true)
        set(value) = pref.edit().putBoolean("tts", value).apply()
    var n1: String
        get() = pref.getString("name1", temp1).toString()
        set(value) = pref.edit().putString("name1", value).apply()
    var n2: String
        get() = pref.getString("name2", temp2).toString()
        set(value) = pref.edit().putString("name2", value).apply()
    var lang: String
        get() = pref.getString("lang", "en").toString()
        set(value) = pref.edit().putString("lang", value).apply()

    fun versionCheck(){
        //just in case i change preference type again
        val ver = BuildConfig.VERSION_CODE
        if (pref.getInt("ver", 0) < ver) {
            pref.edit().clear().apply()
            pref.edit().putInt("ver", ver).apply()}
    }
}