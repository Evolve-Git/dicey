package com.evolve.dicey.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.evolve.dicey.R
import com.evolve.dicey.databinding.ActivityFullscreenBinding
import com.evolve.dicey.logic.Dicey
import com.evolve.dicey.logic.HideSystemUI
import com.evolve.dicey.logic.setLocale
import kotlinx.coroutines.*
import java.util.*

class FullscreenActivity : AppCompatActivity() {
    private var busy = false
    private lateinit var dicey: Dicey

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(setLocale(base))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = DataBindingUtil.setContentView<ActivityFullscreenBinding>(this,
            R.layout.activity_fullscreen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        dicey = Dicey(this, activity.diceyView)

        activity.toolbar.setOnMenuItemClickListener { item: MenuItem? ->
                    when (item!!.itemId) {
                        R.id.action_settings -> {
                            val intent = Intent(this@FullscreenActivity,
                                    SettingsActivity::class.java)
                            //Prevents creation of multiple instances of the Activity.
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            startActivity(intent)
                        }
                    }
                    true
                }

        activity.diceyView.setOnClickListener {
            if (!busy) {
                busy = true
                dicey.animate()

                GlobalScope.launch(Dispatchers.Main) {
                    delay(1100)
                    busy = false
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

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onResume() {
        super.onResume()

        HideSystemUI()
    }

    public override fun onDestroy() {
        super.onDestroy()

        dicey.killTTS()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        HideSystemUI()
    }
}
