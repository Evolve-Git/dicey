
package com.evolve.dicey

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.coroutines.*
import java.util.*
import kotlin.random.Random


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var fullscreenContent: TextView
    private lateinit var fullscreenContentControls: LinearLayout
    private val hideHandler = Handler()

    var busy = false

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
        fullscreenContentControls.visibility = View.VISIBLE
    }
    private var isFullscreen: Boolean = false

    private val hideRunnable = Runnable { hide() }

    lateinit var tts: TextToSpeech

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set UK English as language for tts
            //tts.setLanguage(Locale.US)
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
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) //This line is optional, better to use it because it won't create multiple instances of the launching Activity.
                            startActivity(intent)
                        }
                    }
                    true
                }

        isFullscreen = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = findViewById(R.id.fullscreen_content)

        fullscreenContentControls = findViewById(R.id.fullscreen_content_controls)

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById<Button>(R.id.dummy_button).setOnTouchListener(delayHideTouchListener)

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

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        fullscreenContentControls.visibility = View.GONE
        isFullscreen = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }

}
