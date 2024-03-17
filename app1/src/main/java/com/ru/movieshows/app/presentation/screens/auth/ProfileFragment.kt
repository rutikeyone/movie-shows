package com.ru.movieshows.app.presentation.screens.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.ru.movieshows.app.R
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.viewmodel.auth.ProfileViewModel
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.app.databinding.FragmentProfileBinding
import com.ru.movieshows.sources.accounts.entities.AccountEntity
import com.ru.movieshows.sources.accounts.entities.UserAuthenticationState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    @Inject
    lateinit var factory: ProfileViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(
            navigator = navigator()
        )
    }

    private val binding by viewBinding<FragmentProfileBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() = with(binding) {
        viewModel.authenticationState.observe(viewLifecycleOwner, ::configureUI)
        logoutButton.setOnClickListener { viewModel.logOut() }
    }

    private fun configureUI(state: UserAuthenticationState) = with(binding) {
        root.children.forEach { it.isVisible = false }
        when (state) {
            UserAuthenticationState.Empty -> {}
            is UserAuthenticationState.Pending -> progressGroup.isVisible = true
            is UserAuthenticationState.NotAuthentication -> configureNotAuthenticatedUI()
            is UserAuthenticationState.Authentication -> configureAuthenticatedUI(state.account)
        }
    }

    private fun configureNotAuthenticatedUI() = with(binding) {
        notAuthenticatedGroup.isVisible = true
        binding.signInButton.setOnClickListener {
            viewModel.navigateToSignIn()
        }
    }

    private fun configureAuthenticatedUI(account: AccountEntity) {
        binding.authenticatedGroup.isVisible = true
        configureNameUI(account)
        configureProfileAvatarImageUI(account)
    }


    private fun configureProfileAvatarImageUI(account: AccountEntity) = with(binding.avatarImageView) {
        val photo = account.avatar
        if (!photo.isNullOrEmpty()) {
            Glide
                .with(requireContext())
                .load(photo)
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .centerCrop()
                .into(this)
        } else {
            Glide
                .with(context)
                .load(R.drawable.avatar_placeholder)
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .centerCrop()
                .into(this)
        }
    }

    private fun configureNameUI(account: AccountEntity) = with(binding.nameTextView) {
        val stringBuilder = StringBuilder().also {
            val username = account.username ?: ""
            it.append(username)
        }
        val value = stringBuilder.toString()
        if (value.isNotEmpty()) {
            text = value
            isVisible = true
        } else {
            isVisible = false
        }
    }

}

