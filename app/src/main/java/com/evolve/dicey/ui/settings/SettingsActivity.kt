package com.evolve.dicey.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.evolve.dicey.R
import com.evolve.dicey.databinding.ActivitySettingsBinding
import com.evolve.dicey.logic.SettingsViewModel
import com.evolve.dicey.logic.setLocale


class SettingsActivity: AppCompatActivity() {
    private lateinit var activity: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(setLocale(base))
    }

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity = DataBindingUtil.setContentView(this,
            R.layout.activity_settings)
        setSupportActionBar(activity.settingsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(SettingsViewModel(application)::class.java)
    }

    override fun onBackPressed() {
        NavUtils.navigateUpFromSameTask(this)
        super.onBackPressed()
    }
}
