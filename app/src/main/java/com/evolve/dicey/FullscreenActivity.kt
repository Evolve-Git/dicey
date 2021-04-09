
package com.evolve.dicey

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.coroutines.*
import java.util.*
import kotlin.random.Random

class FullscreenActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var fullscreenContent: TextView

    var busy = false

    lateinit var tts: TextToSpeech

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

        toolbar.setOnMenuItemClickListener { item: MenuItem? ->
                    when (item!!.itemId) {
                        R.id.action_settings -> {
                            val intent = Intent(this@FullscreenActivity, SettingsActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) //Prevents creation of multiple instances of the Activity.
                            startActivity(intent)
                        }
                    }
                    true
                }

        fullscreenContent = findViewById(R.id.fullscreen_content)

        tts = TextToSpeech(this, this)

        val pref: SharedPreferences = this.getSharedPreferences("settings", Context.MODE_PRIVATE)

        fullscreenContent.setOnTouchListener(object : OnSwipeTouchListener(this@FullscreenActivity) {
            override fun onClick() {
                super.onClick()

                var dicey: Array<String> = arrayOf()
                val lang = pref.getInt("lang", R.id.rb1)
                when (lang) {
                    R.id.rb1 -> dicey = resources.getStringArray(R.array.d_en)
                    R.id.rb2 -> dicey = resources.getStringArray(R.array.d_nl)
                    R.id.rb3 -> dicey = resources.getStringArray(R.array.d_ru)
                }
                val is_tts_on = pref.getBoolean("tts", true)
                val n1 = pref.getString("name1", resources.getString(R.string.n1))
                val n2 = pref.getString("name2", resources.getString(R.string.n2))
                val delta = 55
                var temp = "temp"
                if (busy == false) {
                    busy = true
                    val seed = Random.nextInt(6)
                    if ((seed == 0) or (seed == 3)) {
                        temp = dicey[seed]
                    } else if ((seed == 1) or (seed == 2)) {
                        temp = String.format(dicey[seed], n1)
                    } else if ((seed == 4) or (seed == 5)) {
                        temp = String.format(dicey[seed], n2)
                    }

                    if (is_tts_on) {
                        speakOut(temp)
                    }

                    GlobalScope.launch(Dispatchers.Main) {
                        delay((delta * (temp.length) + 1).toLong())
                        busy = false
                    }
                    for (i in 0..temp.length) {
                        GlobalScope.launch(Dispatchers.Main) { // launch a new coroutine in background and continue
                            delay((delta * i).toLong())
                            fullscreenContent.text = temp.subSequence(0, i)
                        }
                    }
                }
            }
            /*
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                val intent = Intent(this@FullscreenActivity, SettingsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) //This line is optional, better to use it because it won't create multiple instances of the launching Activity.
                startActivity(intent)
            }*/
        })
    }

    private fun setTTSLang(){
        val pref: SharedPreferences = this.getSharedPreferences("settings", Context.MODE_PRIVATE)
        var result = 0
        when (pref.getInt("lang", R.id.rb1)) {
            R.id.rb1 -> {result = tts.setLanguage(Locale.US)
                        fullscreenContent.setText(R.string.main_en)}
            R.id.rb2 -> {result = tts.setLanguage(Locale("nl_NL"))
                        fullscreenContent.setText(R.string.main_nl)}
            R.id.rb3 -> {result = tts.setLanguage(Locale("ru_RU"))
                        fullscreenContent.setText(R.string.main_ru)}
        }
        if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts.setLanguage(Locale.getDefault())
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
        // Shutdown TTS
        tts.stop()
        tts.shutdown()
        super.onDestroy()
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
