package com.ru.movieshows.presentation.screens.sign_in

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ru.movieshows.R
import com.ru.movieshows.presentation.viewmodel.sign_in.SignInViewModel

class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private val viewModel by viewModels<SignInViewModel>()
}