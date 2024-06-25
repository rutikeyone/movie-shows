package com.ru.movieshows.movies.presentation.popular

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.ru.movieshows.core.AppException
import com.ru.movieshows.core.Container
import com.ru.movieshows.core.EmptyException
import com.ru.movieshows.core.presentation.BaseFragment
import com.ru.movieshows.core.presentation.applyDecoration
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.core.presentation.views.ItemDecoration
import com.ru.movieshows.core.presentation.views.LoadStateAdapter
import com.ru.movieshows.movies.R
import com.ru.movieshows.movies.databinding.FragmentPopularMoviesBinding
import com.ru.movieshows.movies.presentation.adapters.MoviesPaginationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Suppress("DEPRECATED_IDENTITY_EQUALS")
@AndroidEntryPoint
class PopularMoviesFragment : BaseFragment() {

    override val viewModel by viewModels<PopularMoviesViewModel>()

    private val binding by viewBinding<FragmentPopularMoviesBinding>()

    private val moviesPaginationAdapter by lazy {
        MoviesPaginationAdapter(viewModel)
    }

    private val moviesSpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val oneItemSpan = 1
            val spanCount = getSpanCount()

            val itemViewType = moviesPaginationAdapter.getItemViewType(position)
            val loadingItem = MoviesPaginationAdapter.LOADING_ITEM
            val isLoadingItem = itemViewType === loadingItem
            return if (isLoadingItem) oneItemSpan else spanCount
        }
    }

    private val loadStateLoader: (CombinedLoadStates) -> Unit = { state ->
        setupViewsWhenStateChanged(state)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.fragment_upcoming_movies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        moviesPaginationAdapter.removeLoadStateListener(loadStateLoader)
        super.onDestroyView()
    }

    private fun setupViews() {
        initView()
        collectState()
    }

    private fun initView() {
        val spanCount = getSpanCount()
        val itemDecoration = ItemDecoration(
            spanCount = spanCount
        )

        val tryAgainAction = {
            moviesPaginationAdapter.retry()
        }

        val footerAdapter = LoadStateAdapter(requireContext(), tryAgainAction)
        val gridLayoutManager = GridLayoutManager(context, spanCount).apply {
            spanSizeLookup = moviesSpanSizeLookup
        }

        with(moviesPaginationAdapter) {
            addLoadStateListener(loadStateLoader)
        }

        with(binding.moviesRecyclerView) {
            layoutManager = gridLayoutManager
            adapter = moviesPaginationAdapter.withLoadStateFooter(footerAdapter)
            applyDecoration(itemDecoration)
        }

        binding.root.setTryAgainListener { moviesPaginationAdapter.retry() }

    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.popularMovies.collectLatest { movies ->
                moviesPaginationAdapter.submitData(movies)
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
                val isListEmpty = moviesPaginationAdapter.itemCount == 0

                if (isListEmpty) {
                    val emptyException = EmptyException(R.string.the_list_of_films_is_empty)
                    binding.root.container = Container.Error(emptyException)
                } else {
                    binding.root.container = Container.Success(Unit)
                }

            }
        }
    }

}