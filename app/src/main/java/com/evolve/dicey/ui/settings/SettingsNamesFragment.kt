package com.evolve.dicey.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.evolve.dicey.R
import com.evolve.dicey.databinding.FragmentSettingsNamesBinding
import com.evolve.dicey.logic.SettingsViewModel


class SettingsNamesFragment : Fragment() {
    private lateinit var binding: FragmentSettingsNamesBinding
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
            R.layout.fragment_settings_names,container,false)

        binding.editName1.setText(viewModel.pref.name1)
        binding.editName2.setText(viewModel.pref.name2)

        binding.buttonDef1.setOnClickListener{
            binding.editName1.setText(resources.getString(R.string.name1))
        }

        binding.buttonDef2.setOnClickListener{
            binding.editName2.setText(resources.getString(R.string.name2))
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.pref.name1 = binding.editName1.text.toString()
        viewModel.pref.name2 = binding.editName2.text.toString()
    }
}