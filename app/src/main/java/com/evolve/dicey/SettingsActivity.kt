package com.evolve.dicey

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

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

        val pref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val prefeditor = pref.edit()

        edit1.setText(pref.getString("name1", resources.getString(R.string.n1)))
        edit2.setText(pref.getString("name2", resources.getString(R.string.n2)))

        edit1.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                prefeditor.putString("name1", edit1.text.toString())
                prefeditor.apply()
            }
        }
        edit2.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                prefeditor.putString("name2", edit2.text.toString())
                prefeditor.apply()
            }
        }/*
        edit2.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                edit2.clearFocus()
                return@OnKeyListener true
            }
            false
        })*/
        edit2.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
                edit2.clearFocus()
                return@OnEditorActionListener true
            }
            false
        })
    }
}