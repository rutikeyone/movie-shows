package com.ru.movieshows.app.presentation.screens.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.ru.movieshows.app.R
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.presentation.adapters.ItemDecoration
import com.ru.movieshows.app.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.app.presentation.adapters.TryAgainAction
import com.ru.movieshows.app.presentation.adapters.movies.MoviesPaginationAdapter
import com.ru.movieshows.app.presentation.adapters.tv_shows.TvShowPaginationAdapter
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.viewmodel.movies.TopRatedMoviesViewModel
import com.ru.movieshows.app.utils.applyDecoration
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.app.databinding.FragmentTopRatedMoviesBinding
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

    private val moviesPaginationAdapter: MoviesPaginationAdapter by lazy {
        MoviesPaginationAdapter(viewModel)
    }

    private val moviesSpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val loadingItem = TvShowPaginationAdapter.LOADING_ITEM
            val isLoadingItem = moviesPaginationAdapter.getItemViewType(position) === loadingItem
            return if (isLoadingItem) 1 else 3
        }
    }

    private val loadStateLoader: LoadStateListener = { state ->
        configureUI(state)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_top_rated_movies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectState()
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.topRatedMovies.collectLatest { movies ->
                moviesPaginationAdapter.submitData(movies)
            }
        }
    }

    private fun initView() {
        val context = requireContext()
        val itemDecoration = ItemDecoration()
        val tryAgainAction: TryAgainAction = { moviesPaginationAdapter.retry() }
        val footerAdapter = LoadStateAdapter(context, tryAgainAction)
        val spanCount = 3

        val gridLayoutManager = GridLayoutManager(context, spanCount).apply {
            spanSizeLookup = moviesSpanSizeLookup
        }

        moviesPaginationAdapter.addLoadStateListener(loadStateLoader)

        with(binding) {
            failurePart.retryButton.setOnClickListener { moviesPaginationAdapter.retry() }
            moviesRecyclerView.layoutManager = gridLayoutManager
            moviesRecyclerView.adapter = moviesPaginationAdapter.withLoadStateFooter(footerAdapter)
            moviesRecyclerView.applyDecoration(itemDecoration)
            moviesRecyclerView.itemAnimator = null
        }
    }

    private fun configureUI(loadState: CombinedLoadStates) {
        val isListEmpty = loadState.refresh is LoadState.NotLoading && moviesPaginationAdapter.itemCount == 0
        val showList = !isListEmpty || loadState.source.refresh is LoadState.NotLoading
        binding.moviesRecyclerView.isVisible = showList
        binding.progressBarMovies.isVisible = loadState.source.refresh is LoadState.Loading
        configureFailurePartUI(loadState)
    }

    private fun configureFailurePartUI(loadState: CombinedLoadStates) {
        binding.failurePart.root.isVisible = loadState.source.refresh is LoadState.Error
        if(loadState.source.refresh !is LoadState.Error) return
        val errorState = loadState.refresh as LoadState.Error
        val error = errorState.error as? AppFailure
        with(binding.failurePart) {
            failureTextHeader.text = resources.getString((error?.headerResource() ?: R.string.error_header))
            failureTextMessage.text = resources.getString(error?.errorResource() ?: R.string.an_error_occurred_during_the_operation)
        }
    }

    override fun onDestroyView() {
        moviesPaginationAdapter.removeLoadStateListener(loadStateLoader)
        super.onDestroyView()
    }

}