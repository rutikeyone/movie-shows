package com.ru.movieshows.presentation.screens.movie_reviews

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentMovieReviewsBinding
import com.ru.movieshows.domain.repository.exceptions.AppFailure
import com.ru.movieshows.presentation.adapters.ReviewsListAdapter
import com.ru.movieshows.presentation.adapters.ReviewsLoadStateAdapter
import com.ru.movieshows.presentation.adapters.TryAgainAction
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.movie_reviews.MovieReviewsViewModel
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieReviewsFragment : BaseFragment(R.layout.fragment_movie_reviews) {
    private val args by navArgs<MovieReviewsFragmentArgs>()

    @Inject
    lateinit var factory: MovieReviewsViewModel.Factory
    override val viewModel by viewModelCreator { factory.create(args.movieId) }

    private val binding by viewBinding<FragmentMovieReviewsBinding>()
    private var adapter: ReviewsListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectUiState()
    }

    private fun initView() {
        adapter = ReviewsListAdapter()
        val tryAgainAction: TryAgainAction = { adapter?.retry() }
        binding.rvReviews.adapter = adapter?.withLoadStateHeaderAndFooter(
            header = ReviewsLoadStateAdapter(tryAgainAction, requireContext()),
            footer = ReviewsLoadStateAdapter(tryAgainAction, requireContext())
        )
        adapter?.addLoadStateListener { loadState -> renderUi(loadState) }
        val itemDecoration = ItemDecoration(resources.displayMetrics)
        binding.rvReviews.addItemDecoration(itemDecoration)
        binding.failurePart.retryButton.setOnClickListener { adapter?.retry() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null;
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.reviews.collectLatest { movies ->
                adapter?.submitData(movies)
            }
        }
    }

    private fun renderUi(loadState: CombinedLoadStates) {
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter?.itemCount == 0
        val showReviews = !isListEmpty || loadState.source.refresh is LoadState.NotLoading
        binding.rvReviews.isVisible = showReviews
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
}

