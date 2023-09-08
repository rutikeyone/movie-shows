package com.ru.movieshows.presentation.screens.top_rated_movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentTopRatedMoviesBinding
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.repository.exceptions.AppFailure
import com.ru.movieshows.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.presentation.adapters.MoviesAdapter
import com.ru.movieshows.presentation.adapters.MoviesListAdapter
import com.ru.movieshows.presentation.adapters.TryAgainAction
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.screens.movie_reviews.ItemDecoration
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.top_rated_movies.TopRatedMoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TopRatedMoviesFragment : BaseFragment(R.layout.fragment_top_rated_movies) {
    override val viewModel by viewModels<TopRatedMoviesViewModel>()
    private val binding by viewBinding<FragmentTopRatedMoviesBinding>()

    private var adapter: MoviesListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectUiState()
    }

    private fun initView() {
        val tryAgainAction: TryAgainAction = { adapter?.retry() }
        val footerAdapter = LoadStateAdapter(tryAgainAction, requireContext())

        adapter = MoviesListAdapter(::navigateToMovieDetails)
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvMovies.layoutManager = gridLayoutManager
        adapter?.addLoadStateListener { loadState -> renderUi(loadState) }
        val itemDecoration = ItemDecoration(metrics = resources.displayMetrics, paddingsInDips = 8F)
        binding.rvMovies.addItemDecoration(itemDecoration)
        binding.failurePart.retryButton.setOnClickListener { adapter?.retry() }
        binding.rvMovies.adapter = adapter!!.withLoadStateFooter(footerAdapter)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter!!.getItemViewType(position) === MoviesAdapter.LOADING_ITEM) 1 else 3
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null;
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.topRatedMovies.collectLatest { movies ->
                adapter?.submitData(movies)
            }
        }
    }

    private fun renderUi(loadState: CombinedLoadStates) {
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter?.itemCount == 0
        val showReviews = !isListEmpty || loadState.source.refresh is LoadState.NotLoading
        binding.rvMovies.isVisible = showReviews
        binding.progressBarMovies.isVisible = loadState.source.refresh is LoadState.Loading
        setupFailurePart(loadState)
    }

    private fun setupFailurePart(loadState: CombinedLoadStates) {
        binding.failurePart.root.isVisible = loadState.source.refresh is LoadState.Error
        if(loadState.source.refresh !is LoadState.Error) return
        val errorState = loadState.refresh as LoadState.Error
        val error = errorState.error as? AppFailure
        binding.failurePart.failureTextHeader.text = resources.getString((error?.headerResource() ?: R.string.error_header))
        binding.failurePart.failureTextMessage.text = resources.getString(error?.errorResource() ?: R.string.an_error_occurred_during_the_operation)
    }

    private fun navigateToMovieDetails(movie: MovieEntity) = viewModel.navigateToMovieDetails(movie)
}