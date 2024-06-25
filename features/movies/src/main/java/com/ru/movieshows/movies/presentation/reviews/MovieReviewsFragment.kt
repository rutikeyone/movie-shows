package com.ru.movieshows.movies.presentation.reviews

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.ru.movieshows.core.AppException
import com.ru.movieshows.core.Container
import com.ru.movieshows.core.EmptyException
import com.ru.movieshows.core.presentation.BaseFragment
import com.ru.movieshows.core.presentation.BaseScreen
import com.ru.movieshows.core.presentation.applyDecoration
import com.ru.movieshows.core.presentation.args
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.core.presentation.viewModelCreator
import com.ru.movieshows.core.presentation.views.ItemDecoration
import com.ru.movieshows.core.presentation.views.LoadStateAdapter
import com.ru.movieshows.movies.R
import com.ru.movieshows.movies.databinding.FragmentMovieReviewsBinding
import com.ru.movieshows.movies.presentation.adapters.ReviewsPaginationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieReviewsFragment : BaseFragment() {

    class Screen(
        val id: String,
    ) : BaseScreen

    @Inject
    lateinit var factory: MovieReviewsViewModel.Factory

    private val binding by viewBinding<FragmentMovieReviewsBinding>()

    override val viewModel by viewModelCreator {
        factory.create(args())
    }

    private val loadStateLoader: (CombinedLoadStates) -> Unit = { state ->
        setupViewsWhenStateChanged(state)
    }

    private val reviewsPaginationAdapter by lazy {
        ReviewsPaginationAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_movie_reviews, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        reviewsPaginationAdapter.removeLoadStateListener(loadStateLoader)
        super.onDestroyView()
    }

    private fun setupViews() {
        initView()
        collectState()
    }

    private fun initView() {
        val tryAgainAction = {
            reviewsPaginationAdapter.retry()
        }
        val footerAdapter = LoadStateAdapter(requireContext(), tryAgainAction)
        val itemDecoration = ItemDecoration(halfPadding = false)

        with(reviewsPaginationAdapter) {
            addLoadStateListener(loadStateLoader)
        }

        with(binding.reviewsRecyclerView) {
            adapter = reviewsPaginationAdapter.withLoadStateFooter(footerAdapter)
            applyDecoration(itemDecoration)
        }

        binding.root.setTryAgainListener { reviewsPaginationAdapter.retry() }

    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movieReviews.collectLatest { reviews ->
                reviewsPaginationAdapter.submitData(reviews)
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun setupViewsWhenStateChanged(state: CombinedLoadStates) {
        when (val refreshState = state.refresh) {
            is LoadState.Error -> {
                val error = refreshState.error
                val appException = AppException(cause = error)
                val errorContainer = Container.Error(appException)

                binding.root.container = errorContainer
            }

            LoadState.Loading -> {
                binding.root.container = Container.Pending
            }

            is LoadState.NotLoading -> {
                val isListEmpty = reviewsPaginationAdapter.itemCount == 0

                if (isListEmpty) {
                    val emptyException = EmptyException(R.string.the_list_of_reviews_is_empty)
                    binding.root.container = Container.Error(emptyException)
                } else {
                    binding.root.container = Container.Success(Unit)
                }

            }
        }
    }

}