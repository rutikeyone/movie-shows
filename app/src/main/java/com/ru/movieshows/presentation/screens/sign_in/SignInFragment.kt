package com.ru.movieshows.presentation.screens.sign_in

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import com.ru.movieshows.databinding.FragmentSignInBinding
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.viewmodel.sign_in.SignInViewModel
import com.ru.movieshows.presentation.viewmodel.sign_in.state.SignInState
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment() {
    @Inject
    lateinit var factory: SignInViewModel.Factory
    override val viewModel by viewModelCreator {
        factory.create(
            resources = resources(),
            toasts = toasts()
        )
    }

    private var _binding: FragmentSignInBinding? = null
    val binding get() = _binding!!

    private val signInOnClickListener = View.OnClickListener {
        hideKeyboard()
        viewModel.signIn()
    }

    private val usernameTextWatched = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val value = s.toString()
            viewModel.changeUsername(value)
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private val passwordTextWatched = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val value = s.toString()
            viewModel.changePassword(value)
        }

        override fun afterTextChanged(s: Editable?) {}

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentSignInBinding.inflate(inflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleUIWhenStateChanged)
        configureUI()
    }

    override fun onStart() {
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        super.onStart()
    }

    override fun onPause() {
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onPause()
    }

    override fun onDestroy() = with(binding) {
        usernameEditText.removeTextChangedListener(usernameTextWatched)
        passwordEditText.removeTextChangedListener(passwordTextWatched)
        _binding = null
        super.onDestroy()
    }

    private fun configureUI() = with(binding) {
        signInButton.setOnClickListener(signInOnClickListener)
        usernameEditText.addTextChangedListener(usernameTextWatched)
        passwordEditText.addTextChangedListener(passwordTextWatched)
    }

    private fun handleUIWhenStateChanged(state: SignInState) = with(binding) {
        val usernameError = validateUsername(state.email)
        usernameTextInput.error = usernameError
        passwordTextInput.error = validatePassword(state.password)
        progressBar.isVisible = state.signInInProgress
        signInButton.isEnabled = state.canSignIn
    }
}
