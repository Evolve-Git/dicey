package com.evolve.dicey.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.evolve.dicey.R
import com.evolve.dicey.databinding.FragmentSettingsNamesBinding
import com.evolve.dicey.logic.SettingsViewModel

class SettingsNamesFragment : Fragment() {
    private lateinit var binding: FragmentSettingsNamesBinding
    private val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_settings_names,container,false)
        binding.settingsViewModel = viewModel

        binding.buttonDef1.setOnClickListener{
            binding.editName1.setText(resources.getString(R.string.name1))
        }

        binding.buttonDef2.setOnClickListener{
            binding.editName2.setText(resources.getString(R.string.name2))
        }

        return binding.root
    }
}