package com.evolve.dicey.logic

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    val pref = Prefs(application.applicationContext)
}