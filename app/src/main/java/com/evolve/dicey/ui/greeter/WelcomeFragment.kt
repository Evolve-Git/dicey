package com.evolve.dicey.ui.greeter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.evolve.dicey.R
import com.evolve.dicey.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentWelcomeBinding>(inflater,
            R.layout.fragment_welcome,container,false)
        binding.buttonWelcomeNext.setOnClickListener{
            view?.findNavController()?.navigate(R.id.action_welcomeFragment_to_welcomeNamesFragment)
        }
        return binding.root
    }
}