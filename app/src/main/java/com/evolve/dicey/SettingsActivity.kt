package com.evolve.dicey

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.SharedPreferences
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText

class SettingsActivity: AppCompatActivity() {
    private lateinit var settingsContent: ScrollView
    private lateinit var settingsContentControls: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settings)
        val toolbar: Toolbar = findViewById(R.id.toolbar3)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settingsContent = findViewById(R.id.setts)
        settingsContentControls = findViewById(R.id.sets)

        val button: Button = findViewById(R.id.button)
        val edit1: TextInputEditText = findViewById(R.id.edit1)
        val edit2: TextInputEditText = findViewById(R.id.edit2)

        val pref =  getSharedPreferences("settings",Context.MODE_PRIVATE)
        val prefeditor = pref.edit()

        edit1.setText(pref.getString("name1", resources.getString(R.string.n1)))
        edit2.setText(pref.getString("name2", resources.getString(R.string.n2)))

        button.setOnClickListener {
            prefeditor.putString("name1",edit1.text.toString())
            prefeditor.putString("name2",edit2.text.toString())
            prefeditor.apply()
        }
    }
}