package com.ru.movieshows.tv_shows.presentation.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ru.movieshows.core.presentation.BaseFragment
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.tvshows.R
import com.ru.movieshows.tvshows.databinding.FragmentPopularTvShowsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularTvShowsFragment : BaseFragment() {

    override val viewModel by viewModels<PopularTvShowsViewModel>()

    private val binding by viewBinding<FragmentPopularTvShowsBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_popular_tv_shows, container, false)

}