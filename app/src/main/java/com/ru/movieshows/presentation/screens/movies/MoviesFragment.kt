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
import androidx.fragment.app.viewModels
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentMoviesBinding
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.adapters.MoviesAdapter
import com.ru.movieshows.presentation.adapters.MoviesViewPagerAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.screens.movie_reviews.ItemDecoration
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.movies.MoviesDiscoverState
import com.ru.movieshows.presentation.viewmodel.movies.MoviesState
import com.ru.movieshows.presentation.viewmodel.movies.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MoviesFragment : BaseFragment() {
    private var adapter: MoviesAdapter? = null
    private val binding by viewBinding<FragmentMoviesBinding>()

    override val viewModel by viewModels<MoviesViewModel>()

    private val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            val index = tab?.position ?: return
            viewModel.changeTabIndex(index)
        }
        override fun onTabUnselected(tab: TabLayout.Tab?) {}
        override fun onTabReselected(tab: TabLayout.Tab?) {}
    }

    private val toolbar: MaterialToolbar get() = requireActivity().findViewById(R.id.tabsToolbar)

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
        viewModel.state.observe(viewLifecycleOwner, ::renderUI)
        viewModel.discoverMoviesState.observe(viewLifecycleOwner, ::renderDiscoverMoviesUI)
    }

    override fun onStart() {
        toolbar.addMenuProvider(menuProvider, viewLifecycleOwner)
        super.onStart()
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
        val itemDecorator = ItemDecoration(8F, resources.displayMetrics)
        if(adapter == null) {
            adapter = MoviesAdapter(::navigateToMovieDetails)
        }

        while (binding.discoverMovies.itemDecorationCount > 0) {
            binding.discoverMovies.removeItemDecorationAt(0);
        }

        adapter?.updateData(movies)
        binding.discoverMovies.addItemDecoration(itemDecorator)
        binding.discoverMovies.adapter = adapter
        binding.discoverMoviesSuccessContainer.visibility = View.VISIBLE
    }

    private fun renderDiscoverMoviesInPendingUI() {
        binding.discoverMoviesInPendingContainer.visibility = View.VISIBLE
    }

    override fun onStop() {
        binding.genresTabLayout.removeOnTabSelectedListener(tabSelectedListener)
        toolbar.removeMenuProvider(menuProvider)
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
        val itemDecorator = ItemDecoration(8F, resources.displayMetrics)
        val adapter = MoviesAdapter(::navigateToMovieDetails).also { it.updateData(state.topRatedMovies) }
        binding.topRatedMovies.adapter = adapter
        binding.showAllTopRatedMoviesButton.setOnClickListener { viewModel.navigateToTopRatedMovies() }
        binding.topRatedMovies.addItemDecoration(itemDecorator)
    }


    private fun setupPopularMoviesRecyclerView(state: MoviesState.Success) {
        val itemDecorator = ItemDecoration(8F, resources.displayMetrics)
        val adapter = MoviesAdapter(::navigateToMovieDetails).also { it.updateData(state.popularMovies) }
        binding.popularMovies.adapter = adapter
        binding.showAllPopularVideosButton.setOnClickListener { viewModel.navigateToPopularMovies() }
        binding.popularMovies.addItemDecoration(itemDecorator)
    }

    private fun setupUpcomingMoviesRecyclerView(state: MoviesState.Success) {
        val itemDecorator = ItemDecoration(8F, resources.displayMetrics)
        val adapter = MoviesAdapter(::navigateToMovieDetails).also { it.updateData(state.upcomingMovies) }
        binding.upcomingMovies.adapter = adapter
        binding.showAllUpcomingVideosButton.setOnClickListener { viewModel.navigateToUpcomingMovies() }
        binding.upcomingMovies.addItemDecoration(itemDecorator)
    }

    private fun setupNowPlayingPager(state: MoviesState.Success) {
        val notPlayingAdapter = MoviesViewPagerAdapter(state.nowPlayingMovies, ::navigateToMovieDetails)
        binding.nowPlayingViewPager.adapter = notPlayingAdapter
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

    private fun navigateToMovieSearch() = viewModel.navigateToMovieSearch()

}

