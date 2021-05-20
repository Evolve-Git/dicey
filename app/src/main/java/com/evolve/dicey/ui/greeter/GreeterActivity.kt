package com.evolve.dicey.ui.greeter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.evolve.dicey.R
import com.evolve.dicey.databinding.ActivityGreeterBinding
import com.evolve.dicey.logic.HideSystemUI
import com.evolve.dicey.logic.setLocale

class GreeterActivity : AppCompatActivity() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityGreeterBinding>(this, R.layout.activity_greeter)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        HideSystemUI()
    }

    override fun onResume() {
        super.onResume()

        HideSystemUI()
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