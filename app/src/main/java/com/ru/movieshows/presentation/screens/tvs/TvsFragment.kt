package com.ru.movieshows.presentation.screens.tvs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.MenuProvider
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.appbar.MaterialToolbar
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentTvsBinding
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.adapters.TvShowsAdapter
import com.ru.movieshows.presentation.adapters.TvShowsViewPagerAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.screens.movie_reviews.ItemDecoration
import com.ru.movieshows.presentation.utils.Listener
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.tv_shows.TvShowsState
import com.ru.movieshows.presentation.viewmodel.tv_shows.TvShowsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvsFragment : BaseFragment() {
    override val viewModel by viewModels<TvShowsViewModel>()
    private val binding by viewBinding<FragmentTvsBinding>()

    private val toolbar: MaterialToolbar get() = requireActivity().findViewById(R.id.tabsToolbar)

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.search_menu, menu)
        }
        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when(menuItem.itemId) {
            R.id.search -> {
                viewModel.navigateToTvShowSearch()
                true
            }
            else -> false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_tvs, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::renderUI)
    }

    override fun onStart() {
        toolbar.addMenuProvider(menuProvider, viewLifecycleOwner)
        super.onStart()
    }

    override fun onStop() {
        toolbar.removeMenuProvider(menuProvider)
        super.onStop()
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
        setupOnAirTvShowsRecyclerView(successState)
        setupTopRatedTvShowsRecyclerView(successState)
        setupPopularTvShowsRecyclerView(successState)
    }

    private fun renderInPendingUI() {
        binding.inPendingContainer.visibility = View.VISIBLE
    }

    private fun setupTrendingTvShowsPager(state: TvShowsState.Success) {
        val listener = Listener(::navigateToTvShowDetails)
        val adapter = TvShowsViewPagerAdapter(this, state.trendingMovies, listener)
        binding.trendingTvShowsViewPager.adapter = adapter
    }

    private fun setupDots() {
        binding.dotsIndicator.attachTo(binding.trendingTvShowsViewPager)
    }

    private fun setupOnAirTvShowsRecyclerView(state: TvShowsState.Success) {
        val tvShows = state.onAirTvShows
        val itemDecoration = ItemDecoration(8F, resources.displayMetrics)
        val adapter = TvShowsAdapter(tvShows, ::navigateToTvShowDetails)
        binding.onAirTvShows.adapter = adapter
        binding.showAllOnAirTvShowsButton.setOnClickListener { }
        binding.onAirTvShows.addItemDecoration(itemDecoration)
    }

    private fun setupTopRatedTvShowsRecyclerView(state: TvShowsState.Success) {
        val tvShows = state.topRatedTvShows
        val itemDecoration = ItemDecoration(8F, resources.displayMetrics)
        val adapter = TvShowsAdapter(tvShows, ::navigateToTvShowDetails)
        binding.topRatedTvShows.adapter = adapter
        binding.showTopRatedTvShowsButton.setOnClickListener {  }
        binding.topRatedTvShows.addItemDecoration(itemDecoration)
    }

    private fun setupPopularTvShowsRecyclerView(state: TvShowsState.Success) {
        val tvShows = state.popularTvShows
        val itemDecoration = ItemDecoration(8F, resources.displayMetrics)
        val adapter = TvShowsAdapter(tvShows, ::navigateToTvShowDetails)
        binding.popularTvShows.adapter = adapter
        binding.popularTvShowsButton.setOnClickListener {  }
        binding.popularTvShows.addItemDecoration(itemDecoration)
    }

    private fun renderFailureUI(@StringRes header: Int?, @StringRes error: Int?) {
        val failurePartBinding = FailurePartBinding.bind(binding.failurePart.root)
        binding.failureContainer.visibility = View.VISIBLE
        failurePartBinding.retryButton.setOnClickListener { viewModel.fetchTvShowsData() }
        if(header != null) failurePartBinding.failureTextHeader.text = resources.getString(header)
        if(error != null) failurePartBinding.failureTextMessage.text = resources.getString(error)
    }

    private fun navigateToTvShowDetails(tvShows: TvShowsEntity) = viewModel.navigateToTvShowDetails(tvShows)

}
