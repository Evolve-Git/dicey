package com.evolve.dicey.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.widget.SwitchCompat
import com.evolve.dicey.R
import com.evolve.dicey.logic.Prefs
import com.evolve.dicey.logic.setLocale
import java.util.*

class SettingsActivity: AppCompatActivity() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(setLocale(base))
    }

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settings)
        val toolbar: Toolbar = findViewById(R.id.toolbar3)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val edit1: TextInputEditText = findViewById(R.id.edit1)
        val edit2: TextInputEditText = findViewById(R.id.edit2)
        val switchTTS: SwitchCompat = findViewById(R.id.switch1)
        val b1: Button = findViewById(R.id.button1)
        val b2: Button = findViewById(R.id.button2)
        val rg: RadioGroup = findViewById(R.id.rg)
        val pref = Prefs(this)

        edit1.setText(pref.n1)
        edit2.setText(pref.n2)

        b1.setOnClickListener{
            edit1.setText(resources.getString(R.string.n1))
        }

        b2.setOnClickListener{
            edit2.setText(resources.getString(R.string.n2))
        }

        switchTTS.isChecked = pref.isTTSon

        when (Locale.getDefault().language) {
            "nl" -> rg.check(R.id.rb2)
            "ru" -> rg.check(R.id.rb3)
            else -> rg.check(R.id.rb1)
        }
    }

    override fun onPause() {
        super.onPause()
        val edit1: TextInputEditText = findViewById(R.id.edit1)
        val edit2: TextInputEditText = findViewById(R.id.edit2)
        val switchTTS: SwitchCompat = findViewById(R.id.switch1)
        val rg: RadioGroup = findViewById(R.id.rg)
        val pref = Prefs(this)
        pref.n1 = edit1.text.toString()
        pref.n2 = edit2.text.toString()
        pref.isTTSon = switchTTS.isChecked
        pref.lang = findViewById<RadioButton>(rg.checkedRadioButtonId).hint.toString()
    }
}
