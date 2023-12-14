package com.ru.movieshows.presentation.screens.movies

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
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentMoviesBinding
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.adapters.MoviesAdapter
import com.ru.movieshows.presentation.adapters.MoviesViewPagerAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.screens.movie_reviews.ItemDecoration
import com.ru.movieshows.presentation.utils.extension.clearDecorations
import com.ru.movieshows.presentation.viewmodel.viewBinding
import com.ru.movieshows.presentation.viewmodel.movies.MoviesViewModel
import com.ru.movieshows.presentation.viewmodel.movies.state.DiscoverMoviesState
import com.ru.movieshows.presentation.viewmodel.movies.state.MoviesState
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
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

    override fun onStart() {
        navigator().getToolbar()?.addMenuProvider(menuProvider, viewLifecycleOwner)
        super.onStart()
    }

    private fun handleDiscoverMoviesUI(discoverMoviesState: DiscoverMoviesState) {
        binding.discoverMoviesContainer.children.forEach { it.visibility = View.GONE }
        when(discoverMoviesState) {
            DiscoverMoviesState.InPending -> handleDiscoverMoviesInPendingUI()
            is DiscoverMoviesState.Failure -> handleDiscoverMoviesFailureUI(discoverMoviesState.error)
            is DiscoverMoviesState.Success -> handleDiscoverMoviesSuccessUI(discoverMoviesState.movies)
        }
    }

    private fun handleDiscoverMoviesFailureUI(@StringRes error: Int?) = with(binding) {
        discoverMoviesFailureContainer.visibility = View.VISIBLE
        discoverMoviesRetryButton.setOnClickListener { viewModel.fetchDiscoverMovies() }
        if(error != null) binding.discoverMoviesFailureTextMessage.text = resources.getString(error)
    }

    private fun handleDiscoverMoviesSuccessUI(movies: ArrayList<MovieEntity>) {
        with(binding.discoverMovies) {
            val movieAdapter = MoviesAdapter(::navigateToMovieDetails).also { it.updateData(movies) }
            val itemDecorator = ItemDecoration(8F, resources.displayMetrics)
            binding.discoverMovies.clearDecorations()
            addItemDecoration(itemDecorator)
            adapter = movieAdapter
        }
        binding.discoverMoviesSuccessContainer.isVisible = true
    }

    private fun handleDiscoverMoviesInPendingUI() {
        binding.discoverMoviesInPendingContainer.isVisible = true
    }

    override fun onStop() {
        binding.genresTabLayout.removeOnTabSelectedListener(onTabSelectedListener)
        navigator().getToolbar()?.removeMenuProvider(menuProvider)
        super.onStop()
    }

    private fun handleUI(moviesState: MoviesState) {
        binding.root.children.forEach { it.isVisible = false }
        binding.genresTabLayout.removeOnTabSelectedListener(onTabSelectedListener)
        when (moviesState) {
            MoviesState.InPending -> handleInPendingUI()
            is MoviesState.Success -> handleSuccessUI(moviesState)
            is MoviesState.Failure -> handleFailureUI(moviesState.header, moviesState.error)
        }
    }

    private fun handleSuccessUI(state: MoviesState.Success) {
        binding.successContainer.visibility = View.VISIBLE
        configureNowPlayingPager(state)
        configureDots()
        configureGenresTab(state)
        configureUpcomingMoviesRecyclerView(state)
        configurePopularMoviesRecyclerView(state)
        configureTopRatedRecyclerView(state)

    }

    private fun configureTopRatedRecyclerView(state: MoviesState.Success) {
        val itemDecorator = ItemDecoration(8F, resources.displayMetrics)
        val adapter = MoviesAdapter(::navigateToMovieDetails).also { it.updateData(state.topRatedMovies) }
        binding.topRatedMovies.adapter = adapter
        binding.showAllTopRatedMoviesButton.setOnClickListener { viewModel.navigateToTopRatedMovies() }
        binding.topRatedMovies.addItemDecoration(itemDecorator)
    }


    private fun configurePopularMoviesRecyclerView(state: MoviesState.Success) {
        val itemDecorator = ItemDecoration(8F, resources.displayMetrics)
        val adapter = MoviesAdapter(::navigateToMovieDetails).also { it.updateData(state.popularMovies) }
        binding.popularMovies.adapter = adapter
        binding.showAllPopularVideosButton.setOnClickListener { viewModel.navigateToPopularMovies() }
        binding.popularMovies.addItemDecoration(itemDecorator)
    }

    private fun configureUpcomingMoviesRecyclerView(state: MoviesState.Success) {
        val itemDecorator = ItemDecoration(8F, resources.displayMetrics)
        val adapter = MoviesAdapter(::navigateToMovieDetails).also { it.updateData(state.upcomingMovies) }
        binding.upcomingMovies.adapter = adapter
        binding.showAllUpcomingVideosButton.setOnClickListener { viewModel.navigateToUpcomingMovies() }
        binding.upcomingMovies.addItemDecoration(itemDecorator)
    }

    private fun configureNowPlayingPager(state: MoviesState.Success) {
        val notPlayingAdapter = MoviesViewPagerAdapter(state.nowPlayingMovies, ::navigateToMovieDetails)
        binding.nowPlayingViewPager.adapter = notPlayingAdapter
    }

    private fun configureDots() {
        binding.dotsIndicator.attachTo(binding.nowPlayingViewPager)
    }

    private fun configureGenresTab(state: MoviesState.Success) = with(binding) {
        val genres = state.genres
        if(genres.isNotEmpty()) {
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

    private fun navigateToMovieDetails(movieEntity: MovieEntity) = viewModel.navigateToMovieDetails(movieEntity)

    private fun navigateToMovieSearch() = viewModel.navigateToMovieSearch()

}

