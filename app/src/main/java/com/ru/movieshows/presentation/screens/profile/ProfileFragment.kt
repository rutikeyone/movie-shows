package com.ru.movieshows.presentation.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentProfileBinding
import com.ru.movieshows.domain.entity.AccountEntity
import com.ru.movieshows.domain.entity.AuthenticatedState
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    override val viewModel by viewModels<ProfileViewModel>()
    private val binding by viewBinding<FragmentProfileBinding>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false);

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() = with(binding) {
        viewModel.state.observe(viewLifecycleOwner, ::authenticatedStateChanged)
        logoutButton.setOnClickListener { viewModel.logOut() }
    }

    private fun authenticatedStateChanged(authenticatedState: AuthenticatedState) = with(binding) {
        root.children.forEach { it.isVisible = false }
        when (authenticatedState) {
            AuthenticatedState.Pure -> {}
            is AuthenticatedState.InPending -> progressGroup.isVisible = true
            is AuthenticatedState.NotAuthenticated -> authenticatedGroup.isVisible = false
            is AuthenticatedState.Authenticated -> setupAuthenticatedUI(authenticatedState.account)
        }
    }

    private fun setupAuthenticatedUI(account: AccountEntity) = with(binding.authenticatedGroup) {
        setupName(account)
        setupProfileAvatarImage(account)
        isVisible = true
    }

    private fun setupProfileAvatarImage(account: AccountEntity) = with(binding.avatarImageView) {
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
                .with(requireContext())
                .load(R.drawable.avatar_placeholder)
                .centerCrop()
                .into(this)
        }
    }

    private fun setupName(account: AccountEntity) = with(binding.nameTextView) {
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

