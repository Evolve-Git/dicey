package com.evolve.dicey

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.widget.SwitchCompat
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
        val rg: RadioGroup = findViewById(R.id.rg)
        val pref = getSharedPreferences("settings", Context.MODE_PRIVATE)

        edit1.setText(pref.getString("name1", resources.getString(R.string.n1)))
        edit2.setText(pref.getString("name2", resources.getString(R.string.n2)))

        switchTTS.isChecked = pref.getBoolean("tts", true)

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
        val pref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val prefeditor = pref.edit()
        prefeditor.putString("name1", edit1.text.toString())
        prefeditor.putString("name2", edit2.text.toString())
        prefeditor.putBoolean("tts", switchTTS.isChecked)
        prefeditor.putString("lang", findViewById<RadioButton>(rg.checkedRadioButtonId).hint.toString())
        prefeditor.apply()
    }
}
