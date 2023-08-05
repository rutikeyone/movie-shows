package com.ru.movieshows.presentation.screens.movie_reviews

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentMovieReviewsBinding
import com.ru.movieshows.presentation.adapters.DefaultLoadStateAdapter
import com.ru.movieshows.presentation.adapters.ReviewsAdapter
import com.ru.movieshows.presentation.adapters.TryAgainAction
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.simpleScan
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.movie_reviews.MovieReviewsViewModel
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieReviewsFragment : BaseFragment(R.layout.fragment_movie_reviews) {
    @Inject
    lateinit var factory: MovieReviewsViewModel.Factory
    private val args by navArgs<MovieReviewsFragmentArgs>()
    private lateinit var mainLoadStateHolder: DefaultLoadStateAdapter.Holder
    private val binding by viewBinding<FragmentMovieReviewsBinding>()
    override val viewModel by viewModelCreator { factory.create(args.movieId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupReviewList()
    }


    private fun setupReviewList() {
        val adapter = ReviewsAdapter()
        val metrics = resources.displayMetrics
        val tryAgainAction: TryAgainAction = { adapter.retry() }
        val footerAdapter = DefaultLoadStateAdapter(tryAgainAction, requireContext())
        val adapterWithLoadState = adapter.withLoadStateFooter(footerAdapter)
        val itemDecoration = ReviewItemDecoration(metrics)
        binding.loadState.root.children.forEach { it.isVisible = false }
        binding.usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.usersRecyclerView.adapter = adapterWithLoadState
        (binding.usersRecyclerView.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false
        binding.usersRecyclerView.addItemDecoration(itemDecoration)

        mainLoadStateHolder = DefaultLoadStateAdapter.Holder(
            binding.loadState,
            binding.swipeRefreshLayout,
            tryAgainAction,
            requireContext()
        )
        observeReviews(adapter)
        observeLoadState(adapter)
        handleListVisibility(adapter)
        setupSwipeToRefresh(adapter)
    }

    private fun observeReviews(adapter: ReviewsAdapter) = lifecycleScope.launch {
        viewModel.reviewsPaginationFlow.collectLatest {
            adapter.submitData(it)
        }
    }

    private fun observeLoadState(adapter: ReviewsAdapter) = lifecycleScope.launch {
        adapter.loadStateFlow.debounce(200).collectLatest { state ->
            mainLoadStateHolder.bind(state.refresh)
        }
    }

    private fun getRefreshLoadStateFlow(adapter: ReviewsAdapter): Flow<LoadState> {
        return adapter.loadStateFlow.map { it.refresh }
    }

    private fun handleListVisibility(adapter: ReviewsAdapter) = lifecycleScope.launch {
        getRefreshLoadStateFlow(adapter)
            .simpleScan(count = 3)
            .collectLatest { (beforePrevious, previous, current) ->
                binding.usersRecyclerView.isInvisible = current is LoadState.Error
                        || previous is LoadState.Error
                        || (beforePrevious is LoadState.Error && previous is LoadState.NotLoading
                        && current is LoadState.Loading)
            }
    }

    private fun setupSwipeToRefresh(adapter: ReviewsAdapter) {
        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }
    }
}

