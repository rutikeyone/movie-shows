package com.ru.movieshows.app.presentation.screens.movies

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
import com.google.android.material.tabs.TabLayout
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.adapters.movies.MoviesAdapter
import com.ru.movieshows.app.presentation.adapters.movies.MoviesViewPagerAdapter
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.viewmodel.movies.MoviesViewModel
import com.ru.movieshows.app.presentation.viewmodel.movies.state.DiscoverMoviesState
import com.ru.movieshows.app.presentation.viewmodel.movies.state.MovieState
import com.ru.movieshows.app.utils.applyDecoration
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentMoviesBinding
import com.ru.movieshows.sources.movies.entities.MovieEntity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : BaseFragment() {

    private val binding by viewBinding<FragmentMoviesBinding>()

    @Inject
    lateinit var factory: MoviesViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(
            navigator = navigator()
        )
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            val index = tab?.position ?: return
            viewModel.changeTabIndex(index)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {}

        override fun onTabReselected(tab: TabLayout.Tab?) {}
    }

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.search_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when(menuItem.itemId) {
            R.id.search -> {
                navigateToMovieSearch()
                true
            }
            else -> false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.moviesState.observe(viewLifecycleOwner, ::handleUI)
        viewModel.discoverMoviesState.observe(viewLifecycleOwner, ::handleDiscoverMoviesUI)
    }

    override fun onStart(){
        navigator().targetNavigator {
            it.getToolbar()?.addMenuProvider(menuProvider, viewLifecycleOwner)
        }
        super.onStart()
    }

    private fun handleDiscoverMoviesUI(state: DiscoverMoviesState) {
        binding.discoverMoviesContainer.children.forEach { it.visibility = View.GONE }
        when(state) {
            DiscoverMoviesState.Empty -> {}
            DiscoverMoviesState.Pending -> handleDiscoverMoviesInPendingUI()
            is DiscoverMoviesState.Failure -> handleDiscoverMoviesFailureUI(state.error)
            is DiscoverMoviesState.Success -> handleDiscoverMoviesSuccessUI(state.movies)
        }
    }

    private fun handleDiscoverMoviesFailureUI(@StringRes error: Int?) {
        with(binding) {
            discoverMoviesFailureContainer.visibility = View.VISIBLE
            discoverMoviesRetryButton.setOnClickListener { viewModel.fetchDiscoverMovies() }
            if (error != null) binding.discoverMoviesFailureTextMessage.text =
                resources.getString(error)
        }
    }

    private fun handleDiscoverMoviesSuccessUI(movies: ArrayList<MovieEntity>) {
        val moviesAdapter = MoviesAdapter(viewModel).also { it.updateData(movies) }
        with(binding.discoverMovies) {
            applyDecoration()
            adapter = moviesAdapter
        }
        binding.discoverMoviesSuccessContainer.isVisible = true
    }

    private fun handleDiscoverMoviesInPendingUI() {
        binding.discoverMoviesInPendingContainer.isVisible = true
    }

    override fun onStop(){
        binding.genresTabLayout.removeOnTabSelectedListener(onTabSelectedListener)
        navigator().targetNavigator {
            it.getToolbar()?.removeMenuProvider(menuProvider)
        }
        super.onStop()
    }

    private fun handleUI(movieState: MovieState) {
        binding.root.children.forEach { it.isVisible = false }
        binding.genresTabLayout.removeOnTabSelectedListener(onTabSelectedListener)
        when (movieState) {
            MovieState.Empty -> {}
            MovieState.Pending -> handleInPendingUI()
            is MovieState.Success -> handleSuccessUI(movieState)
            is MovieState.Failure -> handleFailureUI(movieState.header, movieState.error)
        }
    }

    private fun handleSuccessUI(state: MovieState.Success) {
        binding.successContainer.visibility = View.VISIBLE
        configureNowPlayingPagerUI(state)
        configureDotsUI()
        configureGenresTabUI(state)
        configureUpcomingMoviesRecyclerViewUI(state)
        configurePopularMoviesRecyclerViewUI(state)
        configureTopRatedRecyclerViewUI(state)

    }

    private fun configureTopRatedRecyclerViewUI(state: MovieState.Success) {
        val adapter = MoviesAdapter(viewModel).also { it.updateData(state.topRatedMovies) }
        binding.topRatedMovies.adapter = adapter
        binding.showAllTopRatedMoviesButton.setOnClickListener { viewModel.navigateToTopRatedMovies() }
        binding.topRatedMovies.applyDecoration()
    }


    private fun configurePopularMoviesRecyclerViewUI(state: MovieState.Success) {
        val adapter = MoviesAdapter(viewModel).also { it.updateData(state.popularMovies) }
        binding.popularMovies.adapter = adapter
        binding.showAllPopularVideosButton.setOnClickListener { viewModel.navigateToPopularMovies() }
        binding.popularMovies.applyDecoration()
    }

    private fun configureUpcomingMoviesRecyclerViewUI(state: MovieState.Success) {
        val adapter = MoviesAdapter(viewModel).also { it.updateData(state.upcomingMovies) }
        binding.upcomingMovies.adapter = adapter
        binding.showAllUpcomingVideosButton.setOnClickListener { viewModel.navigateToUpcomingMovies() }
        binding.upcomingMovies.applyDecoration()
    }

    private fun configureNowPlayingPagerUI(state: MovieState.Success) {
        val notPlayingAdapter = MoviesViewPagerAdapter(state.nowPlayingMovies, viewModel)
        binding.nowPlayingViewPager.adapter = notPlayingAdapter
    }

    private fun configureDotsUI() {
        binding.dotsIndicator.attachTo(binding.nowPlayingViewPager)
    }

    private fun configureGenresTabUI(state: MovieState.Success) = with(binding) {
        val genres = state.genres
        if (genres.isNotEmpty()) {
            state.genres.forEach { genre ->
                val tab = genresTabLayout.newTab().also { tab ->
                    tab.contentDescription = genre.name
                    tab.text = genre.name?.uppercase(Locale.ROOT)
                }
                genresTabLayout.addTab(tab)
            }
            genresTabLayout.getTabAt(viewModel.tabIndexState)?.select()
            genresTabLayout.addOnTabSelectedListener(onTabSelectedListener)
        } else {
            genresTabLayout.visibility = View.GONE
            discoverMoviesContainer.visibility = View.GONE
        }
    }

    private fun handleFailureUI(@StringRes header: Int?, @StringRes error: Int?) {
        val failurePartBinding = FailurePartBinding.bind(binding.failurePart.root)
        binding.failureContainer.visibility = View.VISIBLE
        failurePartBinding.retryButton.setOnClickListener { viewModel.fetchMoviesData() }
        if(header != null) failurePartBinding.failureTextHeader.text = resources.getString(header)
        if(error != null) failurePartBinding.failureTextMessage.text = resources.getString(error)
    }

    private fun handleInPendingUI() {
        binding.inPendingContainer.visibility = View.VISIBLE
    }

    private fun navigateToMovieSearch() = viewModel.navigateToMovieSearch()
}

