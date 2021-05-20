package com.evolve.dicey.ui.greeter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.evolve.dicey.R
import com.evolve.dicey.databinding.FragmentGreeterBinding
import com.evolve.dicey.logic.SettingsViewModel
import com.evolve.dicey.logic.getGreeterData
import com.evolve.dicey.ui.main.FullscreenActivity
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GreeterFragment : Fragment() {
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel(requireActivity().application)::class.java)
        viewModel.pref.versionCheck()
        GlobalScope.launch(Main) {
            delay(3000)
            skipTutorialQuestionMark()
        }
    }

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

        return binding.root
    }

    private fun skipTutorialQuestionMark (){
        if (viewModel.pref.skipTutorial) {
            val intent = Intent(activity, FullscreenActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
        else view?.findNavController()?.navigate(R.id.action_greeterFragment_to_welcomeFragment)
    }
}