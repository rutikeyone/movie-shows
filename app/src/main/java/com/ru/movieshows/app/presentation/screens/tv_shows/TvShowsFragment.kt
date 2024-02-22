package com.ru.movieshows.app.presentation.screens.tv_shows

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
import com.ru.movieshows.app.presentation.adapters.tv_shows.TvShowsAdapter
import com.ru.movieshows.app.presentation.adapters.tv_shows.TvShowsViewPagerAdapter
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.viewmodel.tv_shows.TvShowsViewModel
import com.ru.movieshows.app.presentation.viewmodel.tv_shows.state.TvShowsState
import com.ru.movieshows.app.utils.applyDecoration
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentTvShowsBinding
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
        navigator().targetNavigator {
            it.getToolbar()?.addMenuProvider(menuProvider, viewLifecycleOwner)
        }
        super.onStart()
    }

    override fun onStop() {
        navigator().targetNavigator {
            it.getToolbar()?.removeMenuProvider(menuProvider)
        }
        super.onStop()
    }

    private fun handleUI(state: TvShowsState) {
        binding.root.children.forEach { it.visibility = View.GONE }
        when(state) {
            TvShowsState.Empty -> {}
            TvShowsState.InPending -> configurePendingUI()
            is TvShowsState.Success -> configureSuccessUI(state)
            is TvShowsState.Failure -> configureFailureUI(state.header, state.error)
        }
    }

    private fun configureSuccessUI(successState: TvShowsState.Success) {
        binding.successContainer.isVisible = true 
        configureTrendingTvShowsPager(successState)
        configureDots()
        configureOnAirTvShowsRecyclerView(successState)
        configureTopRatedTvShowsRecyclerView(successState)
        configurePopularTvShowsRecyclerView(successState)
    }

    private fun configurePendingUI() {
        binding.inPendingContainer.visibility = View.VISIBLE
    }

    private fun configureTrendingTvShowsPager(state: TvShowsState.Success) {
        val adapter = TvShowsViewPagerAdapter(state.trendingMovies, viewModel)
        binding.trendingTvShowsViewPager.adapter = adapter
    }

    private fun configureDots() {
        binding.dotsIndicator.attachTo(binding.trendingTvShowsViewPager)
    }

    private fun configureOnAirTvShowsRecyclerView(state: TvShowsState.Success) {
        val tvShows = state.onAirTvShows
        val adapter = TvShowsAdapter(tvShows, viewModel)
        binding.onAirTvShows.adapter = adapter
        binding.showAllOnAirTvShowsButton.setOnClickListener { viewModel.navigateToAirTvShows() }
        binding.onAirTvShows.applyDecoration()
    }

    private fun configureTopRatedTvShowsRecyclerView(state: TvShowsState.Success) {
        val tvShows = state.topRatedTvShows
        val adapter = TvShowsAdapter(tvShows, viewModel)
        binding.topRatedTvShows.adapter = adapter
        binding.showTopRatedTvShowsButton.setOnClickListener { viewModel.navigateToTopRatedTvShows() }
        binding.topRatedTvShows.applyDecoration()
    }

    private fun configurePopularTvShowsRecyclerView(state: TvShowsState.Success) {
        val tvShows = state.popularTvShows
        val adapter = TvShowsAdapter(tvShows, viewModel)
        binding.popularTvShows.adapter = adapter
        binding.popularTvShowsButton.setOnClickListener { viewModel.navigateToPopularTvShows() }
        binding.popularTvShows.applyDecoration()
    }

    private fun configureFailureUI(@StringRes header: Int?, @StringRes error: Int?) {
        val failurePartBinding = FailurePartBinding.bind(binding.failurePart.root)
        binding.failureContainer.visibility = View.VISIBLE
        failurePartBinding.retryButton.setOnClickListener { viewModel.fetchTvShowsData() }
        if(header != null) failurePartBinding.failureTextHeader.text = resources.getString(header)
        if(error != null) failurePartBinding.failureTextMessage.text = resources.getString(error)
    }

}
