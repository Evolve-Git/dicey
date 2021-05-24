package com.evolve.dicey.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.evolve.dicey.R
import com.evolve.dicey.databinding.ActivityFullscreenBinding
import com.evolve.dicey.logic.AnimsManager
import com.evolve.dicey.logic.Dicey
import com.evolve.dicey.logic.hideSystemUI
import com.evolve.dicey.logic.setLocale
import com.evolve.dicey.ui.settings.SettingsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FullscreenActivity : AppCompatActivity() {
    private var busy = false
    private lateinit var animsManager: AnimsManager

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(setLocale(base))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = DataBindingUtil.setContentView<ActivityFullscreenBinding>(this,
            R.layout.activity_fullscreen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        animsManager = AnimsManager(this)
        val dicey = Dicey(this)


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
                animsManager.animate(activity.diceyView, dicey.dice())

                CoroutineScope(Main).launch {
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

        hideSystemUI()
    }

    public override fun onDestroy() {
        super.onDestroy()

        animsManager.killTTS()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        hideSystemUI()
    }

    override fun onStart(){
        super.onStart()

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun finish() {
        super.finish()

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}
