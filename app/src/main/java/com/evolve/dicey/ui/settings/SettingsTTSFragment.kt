package com.evolve.dicey.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.evolve.dicey.R
import com.evolve.dicey.databinding.FragmentSettingsTtsBinding
import com.evolve.dicey.logic.SettingsViewModel

class SettingsTTSFragment : Fragment() {
    private lateinit var binding: FragmentSettingsTtsBinding
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
            R.layout.fragment_settings_tts,container,false)
        binding.ttsSwitch.isChecked = viewModel.pref.isTTSon
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.pref.isTTSon = binding.ttsSwitch.isChecked
    }
}