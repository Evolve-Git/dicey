package com.evolve.dicey.logic

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import java.util.*

class TTS(val context: Context): TextToSpeech.OnInitListener {
    private val tts = TextToSpeech(context, this)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            setTTSLang()
        }
    }

    private fun setTTSLang(){
        val pref = Prefs(context)
        val result = tts.setLanguage(Locale(pref.lang))

        if (result == TextToSpeech.LANG_MISSING_DATA
            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts.language = Locale("en")
        }
    }

    fun speak(temp: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(temp, TextToSpeech.QUEUE_FLUSH, null, "")
        } else {
            @Suppress("DEPRECATION")
            tts.speak(temp, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    fun kill(){
        tts.stop()
        tts.shutdown()
    }
}