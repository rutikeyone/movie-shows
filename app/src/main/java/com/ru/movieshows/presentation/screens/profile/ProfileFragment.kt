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
import com.ru.movieshows.databinding.ProfileBinding
import com.ru.movieshows.domain.entity.AccountEntity
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.main.AuthState
import com.ru.movieshows.presentation.viewmodel.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    override val viewModel by viewModels<ProfileViewModel>()
    private val binding by viewBinding<ProfileBinding>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.profile, container, false);

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() = with(binding) {
        viewModel.state.observe(viewLifecycleOwner, ::authenticatedStateChanged)
        logoutButton.setOnClickListener { viewModel.logOut() }
    }

    private fun authenticatedStateChanged(authState: AuthState) = with(binding) {
        root.children.forEach { it.isVisible = false }
        when (authState) {
            AuthState.Pure -> {}
            AuthState.InPending -> progressGroup.isVisible = true
            is AuthState.Authenticated -> setupAuthenticatedUI(authState.account)
            AuthState.NotAuthenticated -> authenticatedGroup.isVisible = false
        }
    }

    private fun setupAuthenticatedUI(account: AccountEntity) = with(binding) {
        val stringBuilder = StringBuilder().also {
             val username = account.username ?: ""
             it.append(username)
        }
        val value = stringBuilder.toString()
        if(value.isNotEmpty()) nameTextView.text = value

        val photo = account.avatar

        if(!photo.isNullOrEmpty()) {
            Glide
                .with(requireContext())
                .load(photo)
                .centerCrop()
                .into(avatarImageView)
        } else {
            avatarImageView.setImageResource(R.drawable.avatar_placeholder)
        }



        authenticatedGroup.isVisible = true
    }

}

