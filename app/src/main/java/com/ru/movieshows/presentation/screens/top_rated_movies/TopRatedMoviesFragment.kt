package com.ru.movieshows.presentation.screens.top_rated_movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentTopRatedMoviesBinding
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.adapters.ItemDecoration
import com.ru.movieshows.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.presentation.adapters.MoviesAdapter
import com.ru.movieshows.presentation.adapters.MoviesPaginationAdapter
import com.ru.movieshows.presentation.adapters.TryAgainAction
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.viewmodel.top_rated_movies.TopRatedMoviesViewModel
import com.ru.movieshows.presentation.viewmodel.viewBinding
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("DEPRECATED_IDENTITY_EQUALS")
@AndroidEntryPoint
class TopRatedMoviesFragment : BaseFragment() {

    @Inject
    lateinit var factory: TopRatedMoviesViewModel.Factory
    override val viewModel by viewModelCreator {
        factory.create(
            navigator = navigator()
        )
    }

    private val binding by viewBinding<FragmentTopRatedMoviesBinding>()

    private val adapter: MoviesPaginationAdapter = MoviesPaginationAdapter(::navigateToMovieDetails)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_top_rated_movies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        collectUiState()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.topRatedMovies.collectLatest { movies ->
                adapter.submitData(movies)
            }
        }
    }

    private fun initView() = with(binding) {
        adapter.addLoadStateListener { loadState -> configureUI(loadState) }
        val itemDecoration = ItemDecoration(8F, resources.displayMetrics)
        val tryAgainAction: TryAgainAction = { adapter.retry() }
        val footerAdapter = LoadStateAdapter(tryAgainAction, requireContext())
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        failurePart.retryButton.setOnClickListener { adapter.retry() }
        rvMovies.layoutManager = gridLayoutManager
        rvMovies.adapter = adapter.withLoadStateFooter(footerAdapter)
        rvMovies.addItemDecoration(itemDecoration)
        rvMovies.itemAnimator = null
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                    val isLoadingItem = adapter.getItemViewType(position) === MoviesAdapter.LOADING_ITEM
                    return if (isLoadingItem) 1 else 3
            }
        }
    }

    private fun configureUI(loadState: CombinedLoadStates) = with(binding) {
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
        val showList = !isListEmpty || loadState.source.refresh is LoadState.NotLoading
        binding.rvMovies.isVisible = showList
        binding.progressBarMovies.isVisible = loadState.source.refresh is LoadState.Loading
        configureFailurePart(loadState)
    }

    private fun configureFailurePart(loadState: CombinedLoadStates) {
        binding.failurePart.root.isVisible = loadState.source.refresh is LoadState.Error
        if(loadState.source.refresh !is LoadState.Error) return
        val errorState = loadState.refresh as LoadState.Error
        val error = errorState.error as? AppFailure
        with(binding.failurePart) {
            failureTextHeader.text = resources.getString((error?.headerResource() ?: R.string.error_header))
            failureTextMessage.text = resources.getString(error?.errorResource() ?: R.string.an_error_occurred_during_the_operation)
        }
    }

    private fun navigateToMovieDetails(movie: MovieEntity) = viewModel.navigateToMovieDetails(movie)

}