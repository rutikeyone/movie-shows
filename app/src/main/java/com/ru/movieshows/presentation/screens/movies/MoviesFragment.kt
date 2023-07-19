package com.ru.movieshows.presentation.screens.movies

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentMoviesBinding
import com.ru.movieshows.presentation.adapters.NotPlayingViewPagerAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.movies.MoviesState
import com.ru.movieshows.presentation.viewmodel.movies.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MoviesFragment : BaseFragment(R.layout.fragment_movies) {
    override val viewModel by viewModels<MoviesViewModel>()
    private val binding by viewBinding<FragmentMoviesBinding>()
    private val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            val index = tab?.position ?: return
            viewModel.changeTabIndex(index)
        }
        override fun onTabUnselected(tab: TabLayout.Tab?) {}
        override fun onTabReselected(tab: TabLayout.Tab?) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::renderUI)
    }

    override fun onDestroy() {
        binding.genresTabLayout.removeOnTabSelectedListener(tabSelectedListener)
        super.onDestroy()
    }

    private fun renderUI(moviesState: MoviesState) {
        binding.root.children.forEach { it.visibility = View.GONE }
        binding.genresTabLayout.removeOnTabSelectedListener(tabSelectedListener)
        when (moviesState) {
            MoviesState.InPending -> renderInPendingUI()
            is MoviesState.Success -> renderSuccessUI(moviesState)
            is MoviesState.Failure -> renderFailureUI(moviesState.header, moviesState.error)
        }
    }

    private fun renderSuccessUI(state: MoviesState.Success) {
        binding.successContainer.visibility = View.VISIBLE
        val adapter = NotPlayingViewPagerAdapter(this, state.nowPlayingMovies)
        binding.nowPlayingViewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.nowPlayingViewPager)
        state.genres.forEach { genre ->
            val tab = binding.genresTabLayout.newTab().also { tab ->
                tab.contentDescription = genre.name
                tab.text = genre.name?.uppercase(Locale.ROOT)
            }
            binding.genresTabLayout.addTab(tab)
        }
        binding.genresTabLayout.getTabAt(viewModel.tabIndex)?.select()
        binding.genresTabLayout.addOnTabSelectedListener(tabSelectedListener)
    }

    private fun renderFailureUI(@StringRes header: Int?, @StringRes error: Int?) {
        binding.failureContainer.visibility = View.VISIBLE
        binding.retryButton.setOnClickListener { viewModel.fetchMoviesData() }
        if(header != null) binding.failureTextHeader.text = resources.getString(header)
        if(error != null) binding.failureTextMessage.text = resources.getString(error)
    }

    private fun renderInPendingUI() {
        binding.inPendingContainer.visibility = View.VISIBLE
    }
}

