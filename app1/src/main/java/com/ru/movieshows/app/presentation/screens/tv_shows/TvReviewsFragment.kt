package com.ru.movieshows.app.presentation.screens.tv_shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.ru.movieshows.app.R
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.presentation.adapters.ItemDecoration
import com.ru.movieshows.app.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.app.presentation.adapters.TryAgainAction
import com.ru.movieshows.app.presentation.adapters.reviews.ReviewsPaginationAdapter
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.screens.movies.LoadStateListener
import com.ru.movieshows.app.presentation.viewmodel.tv_shows.TvShowReviewsViewModel
import com.ru.movieshows.app.utils.applyDecoration
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.app.databinding.FragmentTvReviewsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TvReviewsFragment : BaseFragment() {

    private val arguments by navArgs<TvReviewsFragmentArgs>()

    @Inject
    lateinit var factory: TvShowReviewsViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(
            tvShowId = arguments.tvShowId
        )
    }

    private val binding by viewBinding<FragmentTvReviewsBinding>()

    private var adapter: ReviewsPaginationAdapter? = null

    private val loadStateLoader: LoadStateListener = { state ->
        configureUI(state)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_tv_reviews, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectState()
    }

    private fun initView() {
        val context = requireContext()
        val itemDecoration = ItemDecoration(halfPadding = false)
        val tryAgainAction: TryAgainAction = { adapter?.retry() }

        adapter = ReviewsPaginationAdapter().apply {
            addLoadStateListener(loadStateLoader)
        }

        with(binding) {
            failurePart.retryButton.setOnClickListener { adapter?.retry() }
            reviewsRecyclerView.adapter = adapter?.withLoadStateFooter(
                LoadStateAdapter(context, tryAgainAction)
            )
            reviewsRecyclerView.applyDecoration(itemDecoration)
        }
    }

    override fun onDestroyView() {
        adapter?.removeLoadStateListener(loadStateLoader)
        adapter = null
        super.onDestroyView()
    }

    private fun collectState() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.reviews.collectLatest { movies ->
            adapter?.submitData(movies)
        }
    }

    private fun configureUI(loadState: CombinedLoadStates) {
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter?.itemCount == 0
        val showReviews = !isListEmpty || loadState.source.refresh is LoadState.NotLoading
        binding.reviewsRecyclerView.isVisible = showReviews
        binding.progressBarMovies.isVisible = loadState.source.refresh is LoadState.Loading
        configureFailurePartUI(loadState)
    }

    private fun configureFailurePartUI(loadState: CombinedLoadStates) = with(binding) {
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