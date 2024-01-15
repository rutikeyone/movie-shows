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
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.ru.movieshows.R
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.presentation.adapters.ItemDecoration
import com.ru.movieshows.app.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.app.presentation.adapters.TryAgainAction
import com.ru.movieshows.app.presentation.adapters.movies.MoviesAdapter
import com.ru.movieshows.app.presentation.adapters.movies.MoviesPaginationAdapter
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.viewmodel.movies.UpcomingMoviesViewModel
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.FragmentUpcomingMoviesBinding
import com.ru.movieshows.sources.movies.entities.MovieEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@Suppress("DEPRECATED_IDENTITY_EQUALS")
@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class UpcomingMoviesFragment : BaseFragment() {

    @Inject
    lateinit var factory: UpcomingMoviesViewModel.Factory
    override val viewModel by viewModelCreator {
        factory.create(
            navigator = navigator(),
        )
    }

    private val binding by viewBinding<FragmentUpcomingMoviesBinding>()

    private var adapter: MoviesPaginationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_upcoming_movies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectUiState()
    }

    private fun initView() {
        val itemDecoration = ItemDecoration(metrics = resources.displayMetrics, paddingsInDips = 8F)
        val tryAgainAction: TryAgainAction = { adapter?.retry() }
        val footerAdapter = LoadStateAdapter(tryAgainAction, requireContext())
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        adapter = MoviesPaginationAdapter(::navigateToMovieDetails)
        binding.rvMovies.layoutManager = gridLayoutManager
        adapter?.addLoadStateListener { loadState -> configureUi(loadState) }
        binding.rvMovies.addItemDecoration(itemDecoration)
        binding.failurePart.retryButton.setOnClickListener { adapter?.retry() }
        binding.rvMovies.adapter = adapter!!.withLoadStateFooter(footerAdapter)
        gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val loadingItem = MoviesAdapter.LOADING_ITEM
                return if (adapter!!.getItemViewType(position) === loadingItem) 1 else 3
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null;
    }

    private fun collectUiState() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.upcomingMovies.collectLatest { movies ->
            adapter?.submitData(movies)
        }
    }

    private fun configureUi(loadState: CombinedLoadStates) {
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter?.itemCount == 0
        val showReviews = !isListEmpty || loadState.source.refresh is LoadState.NotLoading
        binding.rvMovies.isVisible = showReviews
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