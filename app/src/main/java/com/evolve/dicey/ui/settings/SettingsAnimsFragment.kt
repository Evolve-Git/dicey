package com.evolve.dicey.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.evolve.dicey.R
import com.evolve.dicey.databinding.FragmentSettingsAnimsBinding
import com.evolve.dicey.logic.SettingsViewModel
import com.google.android.material.chip.Chip

class SettingsAnimsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsAnimsBinding
    private val viewModel: SettingsViewModel by activityViewModels()
    private var aChips = mutableListOf<Chip>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_settings_anims,container,false)

        val aNames = resources.getStringArray(R.array.anims)
        aChips = mutableListOf()

        for (i in aNames.indices){
            aChips.add(this.layoutInflater.inflate(R.layout.chip, binding.animsGroup, false) as Chip)
            aChips[i].text = aNames[i]
            aChips[i].id = ViewCompat.generateViewId()
            aChips[i].isChecked = viewModel.pref.anims[i]
            aChips[i].setOnCheckedChangeListener{ compoundButton, b ->
                if (b) {
                    aChips[i].isChecked = b
                    uncheckChips(compoundButton)
                }
            }

            binding.animsGroup.addView(aChips[i])
        }
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.pref.anims = getChipsValues()
    }

    private fun getChipsValues(): BooleanArray{
        val tempArray = BooleanArray(aChips.size)
        for (i in aChips.indices){
            tempArray[i] = aChips[i].isChecked
        }
        return tempArray
    }

    private fun uncheckChips(compoundButton: CompoundButton) {
        when (compoundButton.id) {
            aChips[0].id -> for (i in (1 until aChips.size)) aChips[i].isChecked = false
            else -> aChips[0].isChecked = false
        }
    }
}