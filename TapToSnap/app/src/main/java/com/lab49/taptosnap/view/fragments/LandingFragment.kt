package com.lab49.taptosnap.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.lab49.taptosnap.R
import com.lab49.taptosnap.databinding.FragmentLandingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentLandingBinding.inflate(inflater, container, false)
        binding.btnLetsgo.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_landingFragment_to_quizFragment)
        }
        return binding.root
    }
}