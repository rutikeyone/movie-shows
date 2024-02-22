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
import com.ru.movieshows.R
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.presentation.adapters.ItemDecoration
import com.ru.movieshows.app.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.app.presentation.adapters.TryAgainAction
import com.ru.movieshows.app.presentation.adapters.movies.MoviesAdapter
import com.ru.movieshows.app.presentation.adapters.movies.MoviesPaginationAdapter
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.viewmodel.movies.PopularMoviesViewModel
import com.ru.movieshows.app.utils.applyDecoration
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.FragmentPopularMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("DEPRECATED_IDENTITY_EQUALS")
@AndroidEntryPoint
class PopularMoviesFragment : BaseFragment() {

    private val binding by viewBinding<FragmentPopularMoviesBinding>()

    @Inject
    lateinit var factory: PopularMoviesViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(
            navigator = navigator(),
        )
    }

    private var moviesAdapter: MoviesPaginationAdapter? = null

    private val moviesSpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val loadingItem = MoviesAdapter.LOADING_ITEM
            val isLoadingItem = moviesAdapter!!.getItemViewType(position) === loadingItem
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
    ): View? = inflater.inflate(R.layout.fragment_popular_movies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectState()
    }

    private fun initView() {
        val context = requireContext()
        val spanCount = getSpanCount()
        val itemDecoration = ItemDecoration(spanCount = spanCount)
        val tryAgainAction: TryAgainAction = { moviesAdapter?.retry() }
        val footerAdapter = LoadStateAdapter(context, tryAgainAction)

        val gridLayoutManager = GridLayoutManager(context, spanCount).apply {
            spanSizeLookup = moviesSpanSizeLookup
        }
        moviesAdapter = MoviesPaginationAdapter(viewModel).apply {
            addLoadStateListener(loadStateLoader)
        }

        with(binding) {
            failurePart.retryButton.setOnClickListener { moviesAdapter?.retry() }
            moviesRecyclerView.layoutManager = gridLayoutManager
            moviesRecyclerView.applyDecoration(itemDecoration)
            moviesRecyclerView.adapter = moviesAdapter?.withLoadStateFooter(footerAdapter)
            moviesRecyclerView.itemAnimator = null
        }
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.popularMovies.collectLatest { movies ->
                moviesAdapter?.submitData(movies)
            }
        }
    }

    private fun configureUI(loadState: CombinedLoadStates) {
        val isListEmpty = loadState.refresh is LoadState.NotLoading && moviesAdapter?.itemCount == 0
        val showReviews = !isListEmpty || loadState.source.refresh is LoadState.NotLoading
        binding.moviesRecyclerView.isVisible = showReviews
        binding.progressBarMovies.isVisible = loadState.source.refresh is LoadState.Loading
        configureFailurePartUI(loadState)
    }

    private fun configureFailurePartUI(loadState: CombinedLoadStates) {
        binding.failurePart.root.isVisible = loadState.source.refresh is LoadState.Error
        if(loadState.source.refresh !is LoadState.Error) return
        val errorState = loadState.refresh as LoadState.Error
        val error = errorState.error as? AppFailure
        val headerError = error?.headerResource() ?: R.string.error_header
        val messageError = error?.errorResource() ?: R.string.an_error_occurred_during_the_operation
        with(binding.failurePart) {
            failureTextHeader.text = resources.getString(headerError)
            failureTextMessage.text = resources.getString(messageError)
        }
    }

    override fun onDestroyView() {
        moviesAdapter?.removeLoadStateListener(loadStateLoader)
        moviesAdapter = null;
        super.onDestroyView()
    }

}