package com.ru.movieshows.presentation.screens.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentSplashBinding
import com.ru.movieshows.presentation.screens.BaseFragment

class SplashFragment : BaseFragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

}