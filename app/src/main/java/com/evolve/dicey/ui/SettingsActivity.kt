package com.evolve.dicey.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.core.view.ViewCompat
import com.evolve.dicey.R
import com.evolve.dicey.logic.Prefs
import com.evolve.dicey.logic.setLocale
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import java.util.*


class SettingsActivity: AppCompatActivity() {
    private var aChips = mutableListOf<Chip>()
    private lateinit var edit1: TextInputEditText
    private lateinit var edit2: TextInputEditText
    private lateinit var switchTTS: SwitchCompat
    private lateinit var rg: RadioGroup
    private lateinit var cg: ChipGroup
    private lateinit var pref: Prefs

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

        edit1 = findViewById(R.id.edit1)
        edit2 = findViewById(R.id.edit2)
        switchTTS = findViewById(R.id.switch1)
        val b1: Button = findViewById(R.id.button1)
        val b2: Button = findViewById(R.id.button2)
        rg = findViewById(R.id.rg)
        cg = findViewById(R.id.cg)
        pref = Prefs(this)

        edit1.setText(pref.name1)
        edit2.setText(pref.name2)

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

        val aNames = resources.getStringArray(R.array.anims)
        aChips = mutableListOf()

        for (i in aNames.indices){
            aChips.add(this.layoutInflater.inflate(R.layout.chip, cg, false) as Chip)
            aChips[i].text = aNames[i]
            aChips[i].id = ViewCompat.generateViewId()
            aChips[i].isChecked = pref.anims[i]
            aChips[i].setOnCheckedChangeListener{ compoundButton, b ->
                if (b) {
                    aChips[i].isChecked = b
                    solveTheChipCrisis(compoundButton)
                }
            }

            cg.addView(aChips[i])
        }
    }

    override fun onPause() {
        super.onPause()
        pref.name1 = edit1.text.toString()
        pref.name2 = edit2.text.toString()
        pref.isTTSon = switchTTS.isChecked
        pref.lang = findViewById<RadioButton>(rg.checkedRadioButtonId).hint.toString()
        pref.anims = getChipsValues()
    }

    override fun onBackPressed() {
        NavUtils.navigateUpFromSameTask(this)
        super.onBackPressed()
    }

    private fun getChipsValues(): BooleanArray{
        val temp = BooleanArray(aChips.size)
        for (i in aChips.indices){
            temp[i] = aChips[i].isChecked
        }
        return temp
    }

    private fun solveTheChipCrisis(compoundButton: CompoundButton) {
        when (compoundButton.id) {
            aChips[0].id -> for (i in (1 until aChips.size)) aChips[i].isChecked = false
            else -> aChips[0].isChecked = false
        }
    }
}
