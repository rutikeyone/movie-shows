package com.ru.movieshows.signin.presentation.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.ru.movieshows.core.presentation.BaseFragment
import com.ru.movieshows.core.presentation.TextChangedWatcher
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.core.presentation.views.observe
import com.ru.movieshows.signin.R
import com.ru.movieshows.signin.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment() {

    override val viewModel by viewModels<SignInViewModel>()

    private val binding by viewBinding<FragmentSignInBinding>()

    private val usernameTextWatched = object : TextChangedWatcher() {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val value = s.toString()
            viewModel.changeUsernameText(value)
        }
    }

    private val passwordTextWatched = object : TextChangedWatcher() {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val value = s.toString()
            viewModel.changePasswordText(value)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_sign_in, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setupListeners()
            observeScreenState()
        }
    }

    override fun onStart() = with(binding) {
        usernameEditText.addTextChangedListener(usernameTextWatched)
        passwordEditText.addTextChangedListener(passwordTextWatched)
        super.onStart()
    }

    override fun onStop() = with(binding) {
        usernameEditText.removeTextChangedListener(usernameTextWatched)
        passwordEditText.removeTextChangedListener(passwordTextWatched)
        super.onStop()
    }

    private fun FragmentSignInBinding.setupListeners() {
        with(this) {
            skipSignInButton.setOnClickListener { viewModel.launchMain() }
            signInButton.setOnClickListener { viewModel.signIn() }
        }
    }

    private fun FragmentSignInBinding.observeScreenState() {
        root.observe(viewLifecycleOwner, viewModel.stateLiveValue) { state ->
            val usernameError = validateUsername(state.usernameField)
            val passwordError = validatePassword(state.passwordField)

            with(usernameTextInput) {
                isEnabled = state.enableUI
                error = usernameError
            }

            with(passwordTextInput) {
                isEnabled = state.enableUI
                error = passwordError
            }

            with(skipSignInButton) {
                isEnabled = state.enableUI
                isVisible = !state.isTabsNavigationMode
            }

            progressBar.isVisible = state.showProgressBar
            signInButton.isEnabled = state.canSignIn
        }
    }

}
