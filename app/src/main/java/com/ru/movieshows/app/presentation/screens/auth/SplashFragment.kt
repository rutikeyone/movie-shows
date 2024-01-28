package com.ru.movieshows.app.presentation.screens.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.databinding.FragmentSignInBinding
import com.ru.movieshows.databinding.FragmentSplashBinding

class SplashFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_splash, container, false)

}