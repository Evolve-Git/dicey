package com.evolve.dicey.ui.greeter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.evolve.dicey.R
import com.evolve.dicey.databinding.FragmentWelcomeNamesBinding

class WelcomeNamesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentWelcomeNamesBinding>(inflater,
            R.layout.fragment_welcome_names,container,false)

        binding.buttonWelcomeNamesPrev.setOnClickListener{
            activity?.onBackPressed()
        }

        binding.buttonWelcomeNamesNext.setOnClickListener{
            view?.findNavController()?.navigate(R.id.action_welcomeNamesFragment_to_tutorialFragment)
        }
        return binding.root
    }
}