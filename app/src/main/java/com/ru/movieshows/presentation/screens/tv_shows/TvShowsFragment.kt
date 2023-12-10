package com.ru.movieshows.presentation.screens.tv_shows

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
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentTvShowsBinding
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.adapters.TvShowsAdapter
import com.ru.movieshows.presentation.adapters.TvShowsViewPagerAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.screens.movie_reviews.ItemDecoration
import com.ru.movieshows.presentation.utils.extensions.navigator
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.tv_shows.TvShowsViewModel
import com.ru.movieshows.presentation.viewmodel.tv_shows.state.TvShowsState
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TvShowsFragment : BaseFragment() {
    private val binding by viewBinding<FragmentTvShowsBinding>()

    @Inject
    lateinit var factory: TvShowsViewModel.Factory
    override val viewModel by viewModelCreator {
        factory.create(
            navigator = navigator()
        )
    }

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
    ): View? = inflater.inflate(R.layout.fragment_tv_shows, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleUI)
    }

    override fun onStart() {
        navigator().getToolbar()?.addMenuProvider(menuProvider, viewLifecycleOwner)
        super.onStart()
    }

    override fun onStop() {
        navigator().getToolbar()?.removeMenuProvider(menuProvider)
        super.onStop()
    }

    private fun handleUI(tvShowsState: TvShowsState) {
        binding.root.children.forEach { it.visibility = View.GONE }
        when(tvShowsState) {
            is TvShowsState.Failure -> handleFailureUI(tvShowsState.header, tvShowsState.error)
            TvShowsState.InPending -> handleInPendingUI()
            is TvShowsState.Success -> handleSuccessUI(tvShowsState)
        }
    }

    private fun handleSuccessUI(successState: TvShowsState.Success) {
        binding.successContainer.isVisible = true 
        configureTrendingTvShowsPager(successState)
        configureDots()
        configureOnAirTvShowsRecyclerView(successState)
        configureTopRatedTvShowsRecyclerView(successState)
        configurePopularTvShowsRecyclerView(successState)
    }

    private fun handleInPendingUI() {
        binding.inPendingContainer.visibility = View.VISIBLE
    }

    private fun configureTrendingTvShowsPager(state: TvShowsState.Success) {
        val adapter = TvShowsViewPagerAdapter(state.trendingMovies, ::navigateToTvShowDetails)
        binding.trendingTvShowsViewPager.adapter = adapter
    }

    private fun configureDots() {
        binding.dotsIndicator.attachTo(binding.trendingTvShowsViewPager)
    }

    private fun configureOnAirTvShowsRecyclerView(state: TvShowsState.Success) {
        val tvShows = state.onAirTvShows
        val itemDecoration = ItemDecoration(8F, resources.displayMetrics)
        val adapter = TvShowsAdapter(tvShows, ::navigateToTvShowDetails)
        binding.onAirTvShows.adapter = adapter
        binding.showAllOnAirTvShowsButton.setOnClickListener { viewModel.navigateToAirTvShows() }
        binding.onAirTvShows.addItemDecoration(itemDecoration)
    }

    private fun configureTopRatedTvShowsRecyclerView(state: TvShowsState.Success) {
        val tvShows = state.topRatedTvShows
        val itemDecoration = ItemDecoration(8F, resources.displayMetrics)
        val adapter = TvShowsAdapter(tvShows, ::navigateToTvShowDetails)
        binding.topRatedTvShows.adapter = adapter
        binding.showTopRatedTvShowsButton.setOnClickListener { viewModel.navigateToTopRatedTvShows() }
        binding.topRatedTvShows.addItemDecoration(itemDecoration)
    }

    private fun configurePopularTvShowsRecyclerView(state: TvShowsState.Success) {
        val tvShows = state.popularTvShows
        val itemDecoration = ItemDecoration(8F, resources.displayMetrics)
        val adapter = TvShowsAdapter(tvShows, ::navigateToTvShowDetails)
        binding.popularTvShows.adapter = adapter
        binding.popularTvShowsButton.setOnClickListener { viewModel.navigateToPopularTvShows() }
        binding.popularTvShows.addItemDecoration(itemDecoration)
    }

    private fun handleFailureUI(@StringRes header: Int?, @StringRes error: Int?) {
        val failurePartBinding = FailurePartBinding.bind(binding.failurePart.root)
        binding.failureContainer.visibility = View.VISIBLE
        failurePartBinding.retryButton.setOnClickListener { viewModel.fetchTvShowsData() }
        if(header != null) failurePartBinding.failureTextHeader.text = resources.getString(header)
        if(error != null) failurePartBinding.failureTextMessage.text = resources.getString(error)
    }

    private fun navigateToTvShowDetails(tvShows: TvShowsEntity) = viewModel.navigateToTvShowDetails(tvShows)

}
