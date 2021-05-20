package com.evolve.dicey.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.evolve.dicey.R
import com.evolve.dicey.databinding.FragmentSettingsTtsBinding
import com.evolve.dicey.logic.SettingsViewModel

class SettingsTtsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsTtsBinding
    private val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_settings_tts,container,false)
        binding.settingsViewModel = viewModel

        return binding.root
    }
}