package com.ru.movieshows.profile.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.ru.movieshows.core.presentation.BaseFragment
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.core.presentation.views.observe
import com.ru.movieshows.profile.R
import com.ru.movieshows.profile.databinding.FragmentProfileBinding
import com.ru.movieshows.profile.domain.entities.Account
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private val binding by viewBinding<FragmentProfileBinding>()

    override val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setupListeners()
            observeProfile()
        }
    }

    private fun FragmentProfileBinding.setupListeners() {
        logoutButton.setOnClickListener { viewModel.logout() }
        root.setTryAgainListener { viewModel.reload() }
    }

    private fun FragmentProfileBinding.observeProfile() {
        root.observe(viewLifecycleOwner, viewModel.profileLiveValue) { account ->
            setupViews(account)
        }
    }

    private fun setupViews(account: Account) {
        setupProfileView(account)
        setupNameTextView(account)
    }

    private fun setupNameTextView(account: Account) {
        with(binding.nameTextView) {
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

    private fun setupProfileView(account: Account) {
        with(binding.avatarImageView) {
            val avatar = account.avatar
            val loadAvatar =
                if (!avatar.isNullOrEmpty()) avatar
                else R.drawable.avatar_placeholder

            Glide
                .with(requireContext())
                .load(loadAvatar)
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .centerCrop()
                .into(this)
        }
    }

}