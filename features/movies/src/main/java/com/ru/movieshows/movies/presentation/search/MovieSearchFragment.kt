package com.ru.movieshows.movies.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ru.movieshows.core.Core
import com.ru.movieshows.core.flow.simpleScan
import com.ru.movieshows.core.presentation.BaseFragment
import com.ru.movieshows.core.presentation.applyDecoration
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.core.presentation.views.ItemDecoration
import com.ru.movieshows.core.presentation.views.LoadStateAdapter
import com.ru.movieshows.core.presentation.views.TryAgainAction
import com.ru.movieshows.movies.R
import com.ru.movieshows.movies.databinding.FragmentMovieSearchBinding
import com.ru.movieshows.movies.presentation.adapters.MovieSearchListAdapter
import com.ru.movieshows.movies.presentation.adapters.MoviesSearchPaginationAdapter
import com.ru.movieshows.navigation.GlobalNavComponentRouter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MovieSearchFragment : BaseFragment() {

    @Inject
    lateinit var globalNavComponentRouter: GlobalNavComponentRouter

    override val viewModel by viewModels<MovieSearchViewModel>()

    private val binding by viewBinding<FragmentMovieSearchBinding>()

    private var searchView: SearchView? = null

    private var searchItem: MenuItem? = null

    private val movieSearchPaginationAdapter by lazy {
        MoviesSearchPaginationAdapter(viewModel)
    }

    private val movieSearchListAdapter by lazy {
        MovieSearchListAdapter(viewModel)
    }

    private val menuProvider = object : MenuProvider {

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.search_view_menu, menu)
            searchItem = menu.findItem(R.id.search_action)

            searchView = searchItem?.actionView as SearchView?
            searchView?.apply {
                setOnQueryTextListener(onQueryTextListener)
                maxWidth = Int.MAX_VALUE
                imeOptions = EditorInfo.IME_ACTION_DONE
            }
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
            R.id.search_action -> true
            androidx.appcompat.R.id.search_button -> true
            else -> false
        }

    }

    private val onQueryTextListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String): Boolean {
            val isEmpty = query.isEmpty()
            val delay = if (isEmpty) 0L else 500

            val changeQuery = Runnable {
                viewModel.changeQuery(query)
            }

            handler.removeCallbacksAndMessages(null)
            handler.postDelayed(changeQuery, delay)

            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            val isEmpty = newText.isEmpty()
            val delay = if (isEmpty) 0L else 500

            val changeQuery = Runnable {
                viewModel.changeQuery(newText)
            }

            handler.removeCallbacksAndMessages(null)
            handler.postDelayed(changeQuery, delay)

            return true
        }

    }

    private val loadStateListener: (CombinedLoadStates) -> Unit = { state ->
        setupViewsWhenStateChanged(state)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_movie_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupView() {
        initView()
        collectState()
        createMenu()
        handleScrollingToTopWhenSearching()
    }

    private fun addMenuProvider() {
        globalNavComponentRouter.getToolbar()?.addMenuProvider(menuProvider)
    }

    private fun createMenu() {
        addMenuProvider()

        val query = viewModel.queryStateValue
        if (query != null) {
            searchItem?.expandActionView()
            searchView?.setQuery(query, false)
        }
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.searchMoviesPagedFlowState.collect { movies ->
                    movieSearchPaginationAdapter.submitData(movies)
                }
            }

            launch {
                viewModel.moviesSearchStateFlow.collect { moviesSearch ->
                    movieSearchListAdapter.submitList(moviesSearch)
                }
            }
        }
    }

    private fun initView() {
        val itemDecorator = ItemDecoration(halfPadding = false)
        val tryAgainAction: TryAgainAction = { movieSearchPaginationAdapter.retry() }
        val footerAdapter = LoadStateAdapter(requireContext(), tryAgainAction)

        with(binding) {
            root.forEach {
                it.isVisible = false
            }

            failurePart.retryButton.setOnClickListener { movieSearchPaginationAdapter.retry() }
        }

        with(binding.moviesRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MovieSearchFragment.movieSearchPaginationAdapter.withLoadStateFooter(
                footerAdapter
            )
            applyDecoration(itemDecorator)
            itemAnimator = null
        }

        movieSearchPaginationAdapter.addLoadStateListener(loadStateListener)

        with(binding.moviesSearchRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MovieSearchFragment.movieSearchListAdapter
            applyDecoration()
        }

    }

    private fun setupViewsWhenStateChanged(loadState: CombinedLoadStates) {
        with(binding) {
            val searchMode = viewModel.isSearchMode
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && movieSearchPaginationAdapter.itemCount == 0
            val isNotLoading = loadState.source.refresh is LoadState.NotLoading
            val isLoading = loadState.source.refresh is LoadState.Loading
            val showMovies = !isListEmpty && isNotLoading && searchMode

            successGroup.isVisible = showMovies
            progressGroup.isVisible = isLoading && searchMode
            successEmptyGroup.isVisible = isNotLoading && searchMode && isListEmpty
            searchHistoryGroup.isVisible = !searchMode

            setupFailureView(loadState, searchMode)
        }
    }

    private fun setupFailureView(loadState: CombinedLoadStates, searchMode: Boolean) {
        with(binding) {
            val isErrorState = loadState.source.refresh is LoadState.Error
            val showFailure = isErrorState && searchMode

            failureGroup.isVisible = showFailure

            if (!isErrorState) return

            val errorState = loadState.refresh as? LoadState.Error
            errorState?.error?.let {
                val headerError = Core.errorHandler.getUserHeader(it)
                val messageError = Core.errorHandler.getUserMessage(it)

                with(failurePart) {
                    failureHeaderTextView.text = headerError
                    failureMessageTextView.text = messageError
                }
            }
        }
    }

    private fun handleScrollingToTopWhenSearching() {
        viewLifecycleOwner.lifecycleScope.launch {
            getRefreshLoadStateFlow().simpleScan(count = 2)
                .collectLatest { (previousState, currentState) ->
                    if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                        binding.moviesRecyclerView.scrollToPosition(0)
                    }
                }
        }
    }

    private fun removeMenuProvider() {
        globalNavComponentRouter.getToolbar()?.removeMenuProvider(menuProvider)
    }

    private fun getRefreshLoadStateFlow(): Flow<LoadState> {
        return movieSearchPaginationAdapter.loadStateFlow.map { it.refresh }
    }

    private fun onDestroyMenu() {
        searchView?.setOnQueryTextListener(null)
        searchItem = null
        searchView = null
        removeMenuProvider()
    }

    override fun onDestroyView() {
        movieSearchPaginationAdapter.removeLoadStateListener(loadStateListener)
        onDestroyMenu()
        super.onDestroyView()
    }

}
