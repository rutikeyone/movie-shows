package com.ru.movieshows.app.presentation.screens.movies

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
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.presentation.adapters.ItemDecoration
import com.ru.movieshows.app.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.app.presentation.adapters.TryAgainAction
import com.ru.movieshows.app.presentation.adapters.reviews.ReviewsPaginationAdapter
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.viewmodel.movies.MovieReviewsViewModel
import com.ru.movieshows.app.utils.clearDecorations
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.FragmentMovieReviewsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieReviewsFragment : BaseFragment() {
    private val args by navArgs<MovieReviewsFragmentArgs>()

    @Inject
    lateinit var factory: MovieReviewsViewModel.Factory
    override val viewModel by viewModelCreator {
        factory.create(
            movieId = args.movieId
        )
    }

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
        val itemDecoration = ItemDecoration(metrics = resources.displayMetrics)
        val tryAgainAction: TryAgainAction = { adapter?.retry() }
        val footerAdapter = LoadStateAdapter(tryAgainAction, requireContext())
        adapter = ReviewsPaginationAdapter()
        binding.reviewsRecyclerView.adapter = adapter?.withLoadStateFooter(footerAdapter)
        adapter?.addLoadStateListener { loadState -> handleUI(loadState) }
        with(binding.reviewsRecyclerView) {
            clearDecorations()
            addItemDecoration(itemDecoration)
        }
        binding.failurePart.retryButton.setOnClickListener { adapter?.retry() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null;
    }

    private fun collectUiState() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.reviews.collectLatest { movies ->
            adapter?.submitData(movies)
        }
    }

    private fun handleUI(loadState: CombinedLoadStates) {
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter?.itemCount == 0
        val showReviews = !isListEmpty || loadState.source.refresh is LoadState.NotLoading
        binding.reviewsRecyclerView.isVisible = showReviews
        binding.progressBarMovies.isVisible = loadState.source.refresh is LoadState.Loading
        configureFailurePart(loadState)
    }

    private fun configureFailurePart(loadState: CombinedLoadStates) = with(binding){
        failurePart.root.isVisible = loadState.source.refresh is LoadState.Error
        if(loadState.source.refresh !is LoadState.Error) return
        val errorState = loadState.refresh as LoadState.Error
        val error = errorState.error as? AppFailure
        val headerError = error?.headerResource() ?: R.string.error_header
        val messageError = error?.errorResource() ?: R.string.an_error_occurred_during_the_operation
        failurePart.failureTextHeader.text = resources.getString(headerError)
        failurePart.failureTextMessage.text = resources.getString(messageError)
    }
}

