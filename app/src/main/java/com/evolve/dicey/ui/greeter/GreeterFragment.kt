package com.evolve.dicey.ui.greeter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.evolve.dicey.R
import com.evolve.dicey.databinding.FragmentGreeterBinding
import com.evolve.dicey.logic.getGreeterData

class GreeterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentGreeterBinding>(inflater,
            R.layout.fragment_greeter,container,false)
        binding.greeterImage.background = ResourcesCompat.getDrawable(requireActivity().resources,
            getGreeterData().image, null)
        val greetings = resources.getStringArray(R.array.greetings)
        binding.greeterText.text = String.format(greetings.random(), getGreeterData().greeting)

        binding.greeterImage.setOnClickListener{
            view?.findNavController()?.navigate(R.id.action_greeterFragment_to_welcomeFragment)
        }
        return binding.root
    }

}