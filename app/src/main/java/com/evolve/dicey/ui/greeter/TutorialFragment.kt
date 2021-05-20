package com.evolve.dicey.ui.greeter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.evolve.dicey.R
import com.evolve.dicey.databinding.FragmentTutorialBinding
import com.evolve.dicey.logic.SettingsViewModel
import com.evolve.dicey.ui.main.FullscreenActivity
import com.evolve.dicey.ui.settings.SettingsActivity

class TutorialFragment : Fragment() {
    private val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
    val binding = DataBindingUtil.inflate<FragmentTutorialBinding>(inflater,
        R.layout.fragment_tutorial,container,false)

    binding.buttonTutorialPrev.setOnClickListener{
        activity?.onBackPressed()
    }

    binding.buttonTutorialNext.setOnClickListener{
        finishTutorial()
    }

    binding.tutorialDiceyView.setOnClickListener {
        finishTutorial()
    }

    binding.textTutorial1.setOnClickListener {
        val intent = Intent(activity, SettingsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    return binding.root
}

    private fun finishTutorial(){
        viewModel.pref.skipTutorial = true
        val intent = Intent(activity, FullscreenActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }
}