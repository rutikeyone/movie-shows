package com.ru.movieshows.presentation.screens.tvs

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentTvsBinding
import com.ru.movieshows.presentation.adapters.TvShowsViewPagerAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.tv_shows.TvShowsState
import com.ru.movieshows.presentation.viewmodel.tv_shows.TvShowsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvsFragment : BaseFragment(R.layout.fragment_tvs) {
    override val viewModel by viewModels<TvShowsViewModel>()
    private val binding by viewBinding<FragmentTvsBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::renderUI)
    }

    private fun renderUI(tvShowsState: TvShowsState) {
        binding.root.children.forEach { it.visibility = View.GONE }
        when(tvShowsState) {
            is TvShowsState.Failure -> renderFailureUI(tvShowsState.header, tvShowsState.error)
            TvShowsState.InPending -> renderInPendingUI()
            is TvShowsState.Success -> renderSuccessUI(tvShowsState)
        }
    }

    private fun renderSuccessUI(successState: TvShowsState.Success) {
        binding.successContainer.isVisible = true 
        setupTrendingTvShowsPager(successState)
        setupDots()
    }

    private fun renderInPendingUI() {
        binding.inPendingContainer.visibility = View.VISIBLE
    }

    private fun setupTrendingTvShowsPager(state: TvShowsState.Success) {
        val adapter = TvShowsViewPagerAdapter(this, state.trendingMovies)
        binding.trendingTvShowsViewPager.adapter = adapter
    }

    private fun setupDots() {
        binding.dotsIndicator.attachTo(binding.trendingTvShowsViewPager)
    }

    private fun renderFailureUI(@StringRes header: Int?, @StringRes error: Int?) {
        val failurePartBinding = FailurePartBinding.bind(binding.failurePart.root)
        binding.failureContainer.visibility = View.VISIBLE
        failurePartBinding.retryButton.setOnClickListener { viewModel.fetchTvShowsData() }
        if(header != null) failurePartBinding.failureTextHeader.text = resources.getString(header)
        if(error != null) failurePartBinding.failureTextMessage.text = resources.getString(error)
    }
}
