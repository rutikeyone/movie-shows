package com.ru.movieshows.presentation.screens.movie_reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentMovieReviewsBinding
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.presentation.adapters.ReviewsPaginationAdapter
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
class MovieReviewsFragment : BaseFragment() {
    private val args by navArgs<MovieReviewsFragmentArgs>()

    @Inject
    lateinit var factory: MovieReviewsViewModel.Factory
    override val viewModel by viewModelCreator { factory.create(args.movieId) }

    private val binding by viewBinding<FragmentMovieReviewsBinding>()
    private var adapter: ReviewsPaginationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movie_reviews, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectUiState()
    }

    private fun initView() {
        adapter = ReviewsPaginationAdapter()
        val tryAgainAction: TryAgainAction = { adapter?.retry() }
        binding.rvReviews.adapter = adapter?.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter(tryAgainAction, requireContext()),
            footer = LoadStateAdapter(tryAgainAction, requireContext())
        )
        adapter?.addLoadStateListener { loadState -> renderUi(loadState) }
        val itemDecoration = ItemDecoration(metrics = resources.displayMetrics)
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

    private fun setupFailurePart(loadState: CombinedLoadStates) = with(binding){
        failurePart.root.isVisible = loadState.source.refresh is LoadState.Error
        if(loadState.source.refresh !is LoadState.Error) return
        val errorState = loadState.refresh as LoadState.Error
        val error = errorState.error as? AppFailure
        failurePart.failureTextHeader.text = resources.getString((error?.headerResource() ?: R.string.error_header))
        failurePart.failureTextMessage.text = resources.getString(error?.errorResource() ?: R.string.an_error_occurred_during_the_operation)
    }
}

