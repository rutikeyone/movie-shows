package com.ru.movieshows.app.presentation.screens.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.ru.movieshows.app.R
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.viewmodel.auth.SignInViewModel
import com.ru.movieshows.app.presentation.viewmodel.auth.state.SignInState
import com.ru.movieshows.app.utils.TextChangedWatcher
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.app.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment() {

    @Inject
    lateinit var factory: SignInViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(
            resources = resources(),
            toasts = toasts(),
            navigator = navigator(),
        )
    }

    private val binding by viewBinding<FragmentSignInBinding>()

    private val usernameTextWatched = object : TextChangedWatcher() {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val value = s.toString()
            viewModel.changeUsername(value)
        }
    }

    private val passwordTextWatched = object : TextChangedWatcher() {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val value = s.toString()
            viewModel.changePassword(value)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_sign_in, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleUIWhenStateChanged)
        binding.skipSignInButton.isVisible = !navigator().isNestedRoute()
        configureUI()
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

    private fun configureUI() = with(binding) {
        skipSignInButton.setOnClickListener { viewModel.navigateToTabs() }
        signInButton.setOnClickListener {
            hideKeyboard()
            viewModel.signIn()
        }
    }

    private fun handleUIWhenStateChanged(state: SignInState) = with(binding) {
        val usernameError = validateUsername(state.email)
        usernameTextInput.error = usernameError
        passwordTextInput.error = validatePassword(state.password)
        progressBar.isVisible = state.signInInProgress
        signInButton.isEnabled = state.canSignIn
    }

}
