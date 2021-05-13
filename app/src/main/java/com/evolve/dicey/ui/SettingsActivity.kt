package com.evolve.dicey.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import com.evolve.dicey.R
import com.evolve.dicey.databinding.ActivitySettingsBinding
import com.evolve.dicey.logic.Prefs
import com.evolve.dicey.logic.setLocale
import com.google.android.material.chip.Chip
import java.util.*


class SettingsActivity: AppCompatActivity() {
    private var aChips = mutableListOf<Chip>()
    private lateinit var activity: ActivitySettingsBinding
    private lateinit var pref: Prefs

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(setLocale(base))
    }

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity = DataBindingUtil.setContentView<ActivitySettingsBinding>(this,
            R.layout.activity_settings)
        val toolbar: Toolbar = findViewById(R.id.settings_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pref = Prefs(this)

        activity.editName1.setText(pref.name1)
        activity.editName2.setText(pref.name2)

        activity.buttonDef1.setOnClickListener{
            activity.editName1.setText(resources.getString(R.string.name1))
        }

        activity.buttonDef2.setOnClickListener{
            activity.editName2.setText(resources.getString(R.string.name2))
        }

        activity.ttsSwitch.isChecked = pref.isTTSon

        when (Locale.getDefault().language) {
            "nl" -> activity.langGroup.check(R.id.lang2)
            "ru" -> activity.langGroup.check(R.id.lang3)
            else -> activity.langGroup.check(R.id.lang1)
        }

        val aNames = resources.getStringArray(R.array.anims)
        aChips = mutableListOf()

        for (i in aNames.indices){
            aChips.add(this.layoutInflater.inflate(R.layout.chip, activity.cg, false) as Chip)
            aChips[i].text = aNames[i]
            aChips[i].id = ViewCompat.generateViewId()
            aChips[i].isChecked = pref.anims[i]
            aChips[i].setOnCheckedChangeListener{ compoundButton, b ->
                if (b) {
                    aChips[i].isChecked = b
                    solveTheChipCrisis(compoundButton)
                }
            }

            activity.cg.addView(aChips[i])
        }
    }

    override fun onPause() {
        super.onPause()
        pref.name1 = activity.editName1.text.toString()
        pref.name2 = activity.editName2.text.toString()
        pref.isTTSon = activity.ttsSwitch.isChecked
        pref.lang = findViewById<RadioButton>(activity.langGroup.checkedRadioButtonId).hint.toString()
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
