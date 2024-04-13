package com.ru.movieshows.tv_shows.presentation.tv_shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import com.ru.movieshows.core.presentation.BaseFragment
import com.ru.movieshows.core.presentation.applyDecoration
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.core.presentation.views.observe
import com.ru.movieshows.navigation.GlobalNavComponentRouter
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tv_shows.presentation.adapter.TvShowsAdapter
import com.ru.movieshows.tv_shows.presentation.adapter.TvShowsViewPagerAdapter
import com.ru.movieshows.tvshows.R
import com.ru.movieshows.tvshows.databinding.FragmentTvShowsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TvShowsFragment : BaseFragment() {

    @Inject
    lateinit var globalNavComponentRouter: GlobalNavComponentRouter

    override val viewModel by viewModels<TvShowsViewModel>()

    private val binding by viewBinding<FragmentTvShowsBinding>()

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(com.ru.movieshows.core.presentation.R.menu.search_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
            com.ru.movieshows.core.presentation.R.id.search -> {
                viewModel.launchTvShowSearch()
                true
            }

            else -> false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.fragment_tv_shows, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.root) {
            setTryAgainListener { viewModel.tryToGetTvShowsData() }
            observe(viewLifecycleOwner, viewModel.loadScreenStateLiveValue) {
                setupViews(it)
            }
        }

    }

    override fun onStart() {
        globalNavComponentRouter.getToolbar()?.let {
            it.addMenuProvider(menuProvider, viewLifecycleOwner)
        }
        super.onStart()
    }

    override fun onStop() {
        globalNavComponentRouter.getToolbar()?.let {
            it.removeMenuProvider(menuProvider)
        }
        super.onStop()
    }

    private fun setupViews(state: TvShowsViewModel.State) {
        val trendingTvShows = state.trendingTvShows
        val airTvShows = state.airTvShows
        val topRatedTvShows = state.topRatedTvShows
        val popularTvShows = state.popularTvShows

        setupTrendingTvShowsPagerViews(trendingTvShows)
        setupDotsView()
        setupOnAirTvShowsViews(airTvShows)
        setupTopRatedTvShowsViews(topRatedTvShows)
        setupPopularTvShowsViews(popularTvShows)
    }

    private fun setupTrendingTvShowsPagerViews(tvShows: List<TvShow>) {
        val adapter = TvShowsViewPagerAdapter(tvShows, viewModel)
        binding.trendingTvShowsViewPager.adapter = adapter
    }

    private fun setupDotsView() {
        with(binding.dotsIndicator) {
            this.attachTo(binding.trendingTvShowsViewPager)
        }
    }

    private fun setupOnAirTvShowsViews(tvShows: List<TvShow>) {
        val adapter = TvShowsAdapter(tvShows, viewModel)
        binding.showAllOnAirTvShowsButton.setOnClickListener { viewModel.launchAirTvShows() }
        with(binding.onAirTvShows) {
            this.adapter = adapter
            this.applyDecoration()
        }
    }

    private fun setupTopRatedTvShowsViews(tvShows: List<TvShow>) {
        val adapter = TvShowsAdapter(tvShows, viewModel)
        binding.showTopRatedTvShowsButton.setOnClickListener { viewModel.launchTopRatedTvShows() }
        with(binding.topRatedTvShows) {
            this.adapter = adapter
            this.applyDecoration()
        }
    }

    private fun setupPopularTvShowsViews(tvShows: List<TvShow>) {
        val adapter = TvShowsAdapter(tvShows, viewModel)
        binding.popularTvShowsButton.setOnClickListener { viewModel.launchPopularTvShows() }
        with(binding.popularTvShows) {
            this.adapter = adapter
            this.applyDecoration()
        }
    }

}