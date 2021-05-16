package com.evolve.dicey.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.evolve.dicey.R
import com.evolve.dicey.databinding.FragmentSettingsLangsBinding
import com.evolve.dicey.logic.SettingsViewModel
import java.util.*

class SettingsLangsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsLangsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SettingsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_settings_langs,container,false)
        when (Locale.getDefault().language) {
            "nl" -> binding.langGroup.check(R.id.lang2)
            "ru" -> binding.langGroup.check(R.id.lang3)
            else -> binding.langGroup.check(R.id.lang1)
        }
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.pref.lang = when (binding.langGroup.checkedRadioButtonId) {
            binding.langGroup[1].id -> "nl"
            binding.langGroup[2].id -> "ru"
            else -> "en"
        }
    }
}