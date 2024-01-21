package com.ru.movieshows.app.presentation.screens.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.viewmodel.auth.ProfileViewModel
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.FragmentProfileBinding
import com.ru.movieshows.sources.accounts.entities.AccountEntity
import com.ru.movieshows.sources.accounts.entities.AuthStateEntity
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
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false);

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUI()
    }

    private fun configureUI() = with(binding) {
        viewModel.authState.observe(viewLifecycleOwner, ::handleUI)
        logoutButton.setOnClickListener { viewModel.logOut() }
    }

    private fun handleUI(state: AuthStateEntity) = with(binding) {
        root.children.forEach { it.isVisible = false }
        when (state) {
            AuthStateEntity.Empty -> {}
            is AuthStateEntity.Pending -> progressGroup.isVisible = true
            is AuthStateEntity.NotAuth -> configureNotAuthenticatedUI()
            is AuthStateEntity.Auth -> configureAuthenticatedUI(state.account)
        }
    }

    private fun configureNotAuthenticatedUI() = with(binding) {
        notAuthenticatedGroup.isVisible = true
        binding.signInButton.setOnClickListener {
            viewModel.navigateToSignIn()
        }
    }

    private fun configureAuthenticatedUI(account: AccountEntity) = with(binding.authenticatedGroup) {
        configureName(account)
        configureProfileAvatarImage(account)
        isVisible = true
    }


    private fun configureProfileAvatarImage(account: AccountEntity) = with(binding.avatarImageView) {
        val photo = account.avatar
        if(!photo.isNullOrEmpty()) {
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

    private fun configureName(account: AccountEntity) = with(binding.nameTextView) {
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

