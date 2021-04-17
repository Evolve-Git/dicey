package com.evolve.dicey.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.evolve.dicey.R
import com.evolve.dicey.logic.Prefs
import com.evolve.dicey.logic.setLocale
import kotlinx.coroutines.*
import java.util.*
import kotlin.random.Random

class FullscreenActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var fullscreenContent: TextView
    private var busy = false
    private lateinit  var tts: TextToSpeech

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(setLocale(base))
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            setTTSLang()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        //setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fullscreenContent = findViewById(R.id.fullscreen_content)
        tts = TextToSpeech(this, this)
        val pref = Prefs(this)

        toolbar.setOnMenuItemClickListener { item: MenuItem? ->
                    when (item!!.itemId) {
                        R.id.action_settings -> {
                            val intent = Intent(this@FullscreenActivity,
                                SettingsActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) //Prevents creation of multiple instances of the Activity.
                            startActivity(intent)
                        }
                    }
                    true
                }

        fullscreenContent.setOnClickListener{
            val delta = 55
            val dicey = resources.getStringArray(R.array.dicey)
            var temp = "temp"

            if (!busy) {
                busy = true
                when (val seed = Random.nextInt(6)){
                    0, 3 -> temp = dicey[seed]
                    1, 2 -> temp = String.format(dicey[seed], pref.n1)
                    4, 5 -> temp = String.format(dicey[seed], pref.n2)
                }

                if (pref.isTTSon) {
                    speakOut(temp)
                }

                GlobalScope.launch(Dispatchers.Main) {
                    delay((delta * (temp.length) + 1).toLong())
                    busy = false
                }
                for (i in 0..temp.length) {
                    GlobalScope.launch(Dispatchers.Main) {
                        delay((delta * i).toLong())
                        fullscreenContent.text = temp.subSequence(0, i)
                    }
                }
            }
        }
            /* in case i want to add swipe
            fullscreenContent.setOnTouchListener(object :
            OnSwipeTouchListener(this@FullscreenActivity) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()

            }
        })*/
    }

    private fun setTTSLang(){
        val pref = Prefs(this)
        val result = tts.setLanguage(Locale(pref.lang))

        if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts.language = Locale("en")
        }
    }

    private fun speakOut(temp: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(temp, TextToSpeech.QUEUE_FLUSH, null, "")
        } else {
            @Suppress("DEPRECATION")
            tts.speak(temp, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()

        // Shutdown TTS
        tts.stop()
        tts.shutdown()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        hide()
    }

    private fun hide() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
            window.insetsController?.let {
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                it.hide(WindowInsets.Type.systemBars())
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    // Do not let system steal touches for showing the navigation bar
                    View.SYSTEM_UI_FLAG_LOW_PROFILE or
                            View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }
}
