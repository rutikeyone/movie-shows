package com.ru.movieshows.movies.presentation.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.ru.movieshows.core.Container
import com.ru.movieshows.core.Core
import com.ru.movieshows.core.presentation.BaseFragment
import com.ru.movieshows.core.presentation.TabSelectedListener
import com.ru.movieshows.core.presentation.applyDecoration
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.core.presentation.views.observe
import com.ru.movieshows.movies.R
import com.ru.movieshows.movies.databinding.FragmentMoviesBinding
import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.presentation.views.MoviesAdapter
import com.ru.movieshows.movies.presentation.views.MoviesViewPagerAdapter
import com.ru.movieshows.navigation.GlobalNavComponentRouter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : BaseFragment() {

    @Inject
    lateinit var globalNavComponentRouter: GlobalNavComponentRouter

    override val viewModel by viewModels<MoviesViewModel>()

    private val binding by viewBinding<FragmentMoviesBinding>()

    private val onTabSelectedListener = object : TabSelectedListener() {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            val index = tab?.position ?: return
            viewModel.changeTabIndex(index)
        }
    }

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.search_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
            R.id.search -> {
                viewModel.launchMovieSearch()
                true
            }

            else -> false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_movies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.root) {
            setTryAgainListener { viewModel.toTryToGetMoviesData() }
            observe(viewLifecycleOwner, viewModel.loadScreenStateLiveValue) { state ->
                val index = viewModel.indexStateLiveValue.getValue()
                setupViews(state, index)
            }
        }

        viewModel.loadDiscoverMoviesStateValue.observe(viewLifecycleOwner) {
            setupDiscoverMoviesViews(it)
        }

    }

    override fun onStart() {
        globalNavComponentRouter.getToolbar()?.let {
            it.addMenuProvider(menuProvider, viewLifecycleOwner)
        }
        super.onStart()
    }

    override fun onStop() {
        with(binding.genresTabLayout) {
            removeOnTabSelectedListener(onTabSelectedListener)
        }
        globalNavComponentRouter.getToolbar()?.let {
            it.removeMenuProvider(menuProvider)
        }
        super.onStop()
    }

    private fun setupViews(
        state: MoviesViewModel.State,
        indexState: Int?,
    ) {
        val nowPlayingMovies = state.nowPlayingMovies
        val upcomingMovies = state.upcomingMovies
        val popularMovies = state.popularMovies
        val topRatedMovies = state.topRatedMovies

        setupNowPlayingViewPager(nowPlayingMovies)
        setupDotsView()
        setupGenresTabsView(state, indexState)
        setupUpcomingMoviesViews(upcomingMovies)
        setupPopularMoviesViews(popularMovies)
        setupTopRatedMoviesViews(topRatedMovies)
    }

    private fun setupDiscoverMoviesViews(state: Container<List<Movie>>) {
        binding.discoverMoviesConstraintLayout.children.forEach { it.visibility = View.GONE }
        when (state) {
            Container.Pending -> setupDiscoverMoviesPendingViews()
            is Container.Success -> setupDiscoverMoviesSuccessViews(state)
            is Container.Error -> setupDiscoverMoviesFailureViews(state)
        }
    }

    private fun setupDiscoverMoviesPendingViews() {
        binding.discoverMoviesPendingGroup.isVisible = true
    }

    private fun setupDiscoverMoviesSuccessViews(state: Container.Success<List<Movie>>) {
        val moviesAdapter = MoviesAdapter(viewModel).also { it.updateData(state.value) }
        with(binding.discoverMoviesRecyclerView) {
            adapter = moviesAdapter
            applyDecoration()
        }
        binding.discoverMoviesSuccessGroup.isVisible = true
    }

    private fun setupDiscoverMoviesFailureViews(state: Container.Error) {
        with(binding) {
            val error = Core.errorHandler.getUserMessage(state.exception)
            discoverMoviesFailureGroup.visibility = View.VISIBLE
            discoverMoviesRetryButton.setOnClickListener { viewModel.toTryGetDiscoverMovies() }
            binding.discoverMoviesFailureTextView.text = error
        }
    }

    private fun setupDotsView() {
        with(binding) {
            dotsIndicator.attachTo(nowPlayingViewPager)
        }
    }

    private fun setupNowPlayingViewPager(movies: List<Movie>) {
        with(binding.nowPlayingViewPager) {
            val adapter = MoviesViewPagerAdapter(movies, viewModel)
            this.adapter = adapter
        }
    }

    private fun setupGenresTabsView(
        state: MoviesViewModel.State,
        index: Int?,
    ) = with(binding) {
        val genres = state.genres

        if (genres.isNotEmpty()) {

            genres.forEach { genre ->
                val tab = genresTabLayout.newTab().also { tab ->
                    tab.contentDescription = genre.name
                    tab.text = genre.name?.uppercase(Locale.ROOT)
                }
                genresTabLayout.addTab(tab)
            }

            genresTabLayout.removeOnTabSelectedListener(onTabSelectedListener)
            genresTabLayout.addOnTabSelectedListener(onTabSelectedListener)

            index?.let {
                genresTabLayout.getTabAt(it)?.select()
            }

        } else {
            genresTabLayout.visibility = View.GONE
            discoverMoviesConstraintLayout.visibility = View.GONE
        }
    }

    private fun setupUpcomingMoviesViews(movies: List<Movie>) {
        val adapter = MoviesAdapter(viewModel).also { it.updateData(movies) }
        with(binding) {
            upcomingMoviesRecyclerView.adapter = adapter
            upcomingMoviesRecyclerView.applyDecoration()
            binding.showAllUpcomingVideosButton.setOnClickListener { viewModel.launchUpcomingMovies() }
        }
    }

    private fun setupPopularMoviesViews(movies: List<Movie>) {
        val adapter = MoviesAdapter(viewModel).also { it.updateData(movies) }
        with(binding) {
            popularMoviesRecyclerView.adapter = adapter
            popularMoviesRecyclerView.applyDecoration()
            binding.showAllPopularVideosButton.setOnClickListener { viewModel.launchPopularMovies() }
        }
    }

    private fun setupTopRatedMoviesViews(movies: List<Movie>) {
        val adapter = MoviesAdapter(viewModel).also { it.updateData(movies) }
        with(binding) {
            topRatedMoviesRecyclerView.adapter = adapter
            topRatedMoviesRecyclerView.applyDecoration()
            binding.showAllTopRatedMoviesButton.setOnClickListener { viewModel.launchTopRatedMovies() }
        }
    }

}