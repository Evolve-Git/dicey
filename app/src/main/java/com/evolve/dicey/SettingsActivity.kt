package com.evolve.dicey

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity: AppCompatActivity() {
    private lateinit var settingsContent: ScrollView
    private lateinit var settingsContentControls: LinearLayout

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settings)
        val toolbar: Toolbar = findViewById(R.id.toolbar3)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settingsContent = findViewById(R.id.setts)
        settingsContentControls = findViewById(R.id.sets)

        val edit1: TextInputEditText = findViewById(R.id.edit1)
        val edit2: TextInputEditText = findViewById(R.id.edit2)

        val switch_tts: SwitchCompat = findViewById(R.id.switch1)

        val tx2: TextView = findViewById(R.id.textView2)
        val tx3: TextView = findViewById(R.id.textView3)
        val tx4: TextView = findViewById(R.id.textView4)

        val rg: RadioGroup = findViewById(R.id.rg)

        val pref = getSharedPreferences("settings", Context.MODE_PRIVATE)

        when (pref.getInt("lang", R.id.rb1)){
            R.id.rb1 -> {toolbar.setTitle(R.string.setts_en)
                        tx2.setText(R.string.n_en)
                        tx3.setText(R.string.tts_en)
                        tx4.setText(R.string.lang_en)}
            R.id.rb2 -> {toolbar.setTitle(R.string.setts_nl)
                        tx2.setText(R.string.n_nl)
                        tx3.setText(R.string.tts_nl)
                        tx4.setText(R.string.lang_nl)}
            R.id.rb3 -> {toolbar.setTitle(R.string.setts_ru)
                        tx2.setText(R.string.n_ru)
                        tx3.setText(R.string.tts_ru)
                        tx4.setText(R.string.lang_ru)}
        }

        edit1.setText(pref.getString("name1", resources.getString(R.string.n1)))
        edit2.setText(pref.getString("name2", resources.getString(R.string.n2)))

        switch_tts.isChecked = pref.getBoolean("tts", true)

        rg.check(pref.getInt("lang", R.id.rb1))
    }

    override fun onPause() {
        super.onPause()
        val edit1: TextInputEditText = findViewById(R.id.edit1)
        val edit2: TextInputEditText = findViewById(R.id.edit2)
        val switch_tts: SwitchCompat = findViewById(R.id.switch1)
        val rg: RadioGroup = findViewById(R.id.rg)
        val pref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val prefeditor = pref.edit()
        prefeditor.putString("name1", edit1.text.toString())
        prefeditor.putString("name2", edit2.text.toString())
        prefeditor.putBoolean("tts", switch_tts.isChecked)
        prefeditor.putInt("lang", rg.checkedRadioButtonId)
        prefeditor.apply()
    }
}
