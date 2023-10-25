package com.ru.movieshows.presentation.screens.sign_in

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ru.movieshows.databinding.FragmentSignInBinding
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.hideKeyboard
import com.ru.movieshows.presentation.utils.validateEmail
import com.ru.movieshows.presentation.utils.validatePassword
import com.ru.movieshows.presentation.viewmodel.sign_in.SignInState
import com.ru.movieshows.presentation.viewmodel.sign_in.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment() {
    override val viewModel by viewModels<SignInViewModel>()

    private var _binding: FragmentSignInBinding? = null
    val binding get() = _binding!!

    private val signInOnClickListener = View.OnClickListener {
        hideKeyboard()
        viewModel.signIn()
    }

    private val emailTextWatched = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val value = s.toString()
            viewModel.changeEmail(value)
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
        setupUI()
        observeState()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() = with(binding) {
        emailEditText.removeTextChangedListener(emailTextWatched)
        passwordEditText.removeTextChangedListener(passwordTextWatched)
        _binding = null
        super.onDestroy()
    }

    private fun setupUI() = with(binding) {
        signInButton.setOnClickListener(signInOnClickListener)
        emailEditText.addTextChangedListener(emailTextWatched)
        passwordEditText.addTextChangedListener(passwordTextWatched)
    }

    private fun observeState() =
        viewModel.state.observe(viewLifecycleOwner, ::changeUIWhenStateChanged)

    private fun changeUIWhenStateChanged(state: SignInState) {
        binding.emailEditText.error = validateEmail(state.email)
        binding.passwordEditText.error = validatePassword(state.password)

        binding.signInButton.isEnabled = state.isCanSignIn
        binding.emailTextInput.isEnabled = state.enableView
        binding.passwordTextInput.isEnabled = state.enableView
    }
}
