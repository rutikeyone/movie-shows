package com.ru.movieshows.presentation.screens.movies

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.children
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentMoviesBinding
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.adapters.MoviesAdapter
import com.ru.movieshows.presentation.adapters.MoviesViewPagerAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.movies.MoviesDiscoverState
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
        viewModel.discoverMoviesState.observe(viewLifecycleOwner, ::renderDiscoverMoviesUI)
    }

    private fun renderDiscoverMoviesUI(moviesDiscoverState: MoviesDiscoverState?) {
        binding.discoverMoviesContainer.children.forEach { it.visibility = View.GONE }
        when(moviesDiscoverState) {
            MoviesDiscoverState.InPending -> renderDiscoverMoviesInPendingUI()
            is MoviesDiscoverState.Failure -> renderDiscoverMoviesFailureUI(moviesDiscoverState.error)
            is MoviesDiscoverState.Success -> renderDiscoverMoviesSuccessUI(moviesDiscoverState.movies)
            null -> {}
        }
    }

    private fun renderDiscoverMoviesFailureUI(@StringRes error: Int?) {
        binding.discoverMoviesFailureContainer.visibility = View.VISIBLE
        binding.discoverMoviesRetryButton.setOnClickListener { viewModel.fetchDiscoverMovies() }
        if(error != null) binding.discoverMoviesFailureTextMessage.text = resources.getString(error)
    }

    private fun renderDiscoverMoviesSuccessUI(movies: ArrayList<MovieEntity>) {
        val adapter = MoviesAdapter(movies, ::navigateToMovieDetails)
        binding.discoverMoviesSuccessContainer.visibility = View.VISIBLE
        binding.discoverMovies.adapter = adapter
    }

    private fun renderDiscoverMoviesInPendingUI() {
        binding.discoverMoviesInPendingContainer.visibility = View.VISIBLE
    }

    override fun onStop() {
        binding.genresTabLayout.removeOnTabSelectedListener(tabSelectedListener)
        super.onStop()
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
        setupNowPlayingPager(state)
        setupDots()
        setupGenresTab(state)
        setupUpcomingMoviesRecyclerView(state)
        setupPopularMoviesRecyclerView(state)
        setupTopRatedRecyclerView(state)

    }

    private fun setupTopRatedRecyclerView(state: MoviesState.Success) {
        val adapter = MoviesAdapter(state.topRatedMovies, ::navigateToMovieDetails)
        binding.topRatedMovies.adapter = adapter
        binding.showAllTopRatedMoviesButton.setOnClickListener { viewModel.navigateToTopRatedMovies() }
    }


    private fun setupPopularMoviesRecyclerView(state: MoviesState.Success) {
        val adapter = MoviesAdapter(state.popularMovies, ::navigateToMovieDetails)
        binding.popularMovies.adapter = adapter
        binding.showAllPopularVideosButton.setOnClickListener { viewModel.navigateToPopularMovies() }
    }

    private fun setupUpcomingMoviesRecyclerView(state: MoviesState.Success) {
        val adapter = MoviesAdapter(state.upcomingMovies, ::navigateToMovieDetails)
        binding.upcomingMovies.adapter = adapter
        binding.showAllUpcomingVideosButton.setOnClickListener { viewModel.navigateToUpcomingMovies() }
    }

    private fun setupNowPlayingPager(state: MoviesState.Success) {
        val adapter = MoviesViewPagerAdapter(this, state.nowPlayingMovies)
        binding.nowPlayingViewPager.adapter = adapter
    }

    private fun setupDots() {
        binding.dotsIndicator.attachTo(binding.nowPlayingViewPager)
    }

    private fun setupGenresTab(state: MoviesState.Success) {
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
        val failurePartBinding = FailurePartBinding.bind(binding.failurePart.root)
        binding.failureContainer.visibility = View.VISIBLE
        failurePartBinding.retryButton.setOnClickListener { viewModel.fetchMoviesData() }
        if(header != null) failurePartBinding.failureTextHeader.text = resources.getString(header)
        if(error != null) failurePartBinding.failureTextMessage.text = resources.getString(error)
    }

    private fun renderInPendingUI() {
        binding.inPendingContainer.visibility = View.VISIBLE
    }

    private fun navigateToMovieDetails(movieEntity: MovieEntity) = viewModel.navigateToMovieDetails(movieEntity)
}

