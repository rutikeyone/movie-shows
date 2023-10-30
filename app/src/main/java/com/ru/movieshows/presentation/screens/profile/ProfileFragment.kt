package com.ru.movieshows.presentation.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ru.movieshows.R
import com.ru.movieshows.databinding.ProfileBinding
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.viewBinding
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
        logout.setOnClickListener { viewModel.logOut() }
    }

}

