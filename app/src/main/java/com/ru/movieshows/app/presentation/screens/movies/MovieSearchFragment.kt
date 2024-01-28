package com.ru.movieshows.app.presentation.screens.movies

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
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ru.movieshows.R
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.presentation.adapters.ItemDecoration
import com.ru.movieshows.app.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.app.presentation.adapters.SearchHintAdapter
import com.ru.movieshows.app.presentation.adapters.TryAgainAction
import com.ru.movieshows.app.presentation.adapters.movies.MoviesSearchPaginationAdapter
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.viewmodel.movies.MovieSearchViewModel
import com.ru.movieshows.app.utils.applyDecoration
import com.ru.movieshows.app.utils.simpleScan
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.FragmentMovieSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias LoadStateListener = (CombinedLoadStates) -> Unit

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MovieSearchFragment : BaseFragment() {

    @Inject
    lateinit var factory: MovieSearchViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(
            navigator = navigator(),
        )
    }

    private val binding by viewBinding<FragmentMovieSearchBinding>()

    private var searchView : SearchView? = null

    private var searchItem : MenuItem? = null

    private val moviesSearchAdapter by lazy {
        MoviesSearchPaginationAdapter(viewModel)
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
            val delay = if(isEmpty) 0L else 500

            val changeQuery = Runnable {
                viewModel.changeQuery(query)
            }

            handler.removeCallbacksAndMessages(null)
            handler.postDelayed(changeQuery, delay)

            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            val isEmpty = newText.isEmpty()
            val delay = if(isEmpty) 0L else 500

            val changeQuery = Runnable {
                viewModel.changeQuery(newText)
            }

            handler.removeCallbacksAndMessages(null)
            handler.postDelayed(changeQuery, delay)

            return true
        }

    }

    private val loadStateListener: LoadStateListener = { state ->
        configureUI(state)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_movie_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {
        initMoviesView()
        collectState()
        createMenu()
        handleScrollingToTopWhenSearching()
    }

    private fun addMenuProvider() {
        navigator().targetNavigator {
            it.getToolbar()?.addMenuProvider(menuProvider)
        }
    }

    private fun createMenu() {
        addMenuProvider()
        val query = viewModel.queryValue
        if(query != null) {
            searchItem?.expandActionView()
            searchView?.setQuery(query, false)
        }
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchMovies.collectLatest { movies ->
                moviesSearchAdapter.submitData(movies)
            }
        }
        collectNowPlayingMoviesState()
    }

    private fun collectNowPlayingMoviesState() = viewModel.state.observe(viewLifecycleOwner) { state ->
        val isSearchMode = state.isSearchMode
        val nowPlayingMovies = state.nowPlayingMovies
        val nowPlayingPending = state.nowPlayingInPending
        if (!isSearchMode) {
            binding.maybeYouAreLookingForTextView.isVisible = true
            if(!nowPlayingMovies.isNullOrEmpty() && !nowPlayingPending) {
                val names = nowPlayingMovies.map { it.title }
                val adapter = SearchHintAdapter(names, viewModel)
                with(binding) {
                    searchHintsRecyclerView.isVisible = true
                    searchHintsProgressBar.isVisible = false
                    searchHintsRecyclerView.adapter = adapter
                }
            } else if(nowPlayingMovies.isNullOrEmpty() && nowPlayingPending) {
                with(binding) {
                    searchHintsRecyclerView.isVisible = false
                    searchHintsProgressBar.isVisible = true
                }
            }
        } else {
            with(binding) {
                maybeYouAreLookingForTextView.isVisible = false
                searchHintsRecyclerView.isVisible = false
                searchHintsProgressBar.isVisible = false
            }
        }
    }

    private fun initMoviesView() {
        val context = requireContext()
        val itemDecorator = ItemDecoration(halfPadding = false)
        val tryAgainAction: TryAgainAction = { moviesSearchAdapter.retry() }
        val footerAdapter = LoadStateAdapter(context, tryAgainAction)
        val linearLayoutManager = LinearLayoutManager(context)

        moviesSearchAdapter.addLoadStateListener(loadStateListener)

        with(binding) {
            failurePart.retryButton.setOnClickListener { moviesSearchAdapter.retry() }
            moviesRecyclerView.layoutManager = linearLayoutManager
            moviesRecyclerView.adapter = moviesSearchAdapter.withLoadStateFooter(footerAdapter)
            moviesRecyclerView.applyDecoration(itemDecorator)
            moviesRecyclerView.itemAnimator = null
        }
    }

    private fun configureUI(loadState: CombinedLoadStates) = with(binding) {
        val searchMode = viewModel.isSearchMode
        val isListEmpty = loadState.refresh is LoadState.NotLoading && moviesSearchAdapter.itemCount == 0
        val showMovies = !isListEmpty && loadState.source.refresh is LoadState.NotLoading && searchMode
        moviesRecyclerView.isVisible = showMovies
        progressContainer.isVisible = loadState.source.refresh is LoadState.Loading && searchMode
        successEmptyContainer.isVisible = loadState.source.refresh is LoadState.NotLoading && searchMode && isListEmpty
        configureFailurePartUI(loadState, searchMode)
    }

    private fun configureFailurePartUI(loadState: CombinedLoadStates, searchMode: Boolean) = with(binding) {
        val showFailure = loadState.source.refresh is LoadState.Error && searchMode
        failureContainer.isVisible = showFailure
        if(loadState.source.refresh !is LoadState.Error) return
        val errorState = loadState.refresh as LoadState.Error
        val error = errorState.error as? AppFailure
        val headerError = error?.headerResource() ?: R.string.error_header
        val messageError = error?.errorResource() ?: R.string.an_error_occurred_during_the_operation
        with(failurePart) {
            failureTextHeader.text = resources.getString(headerError)
            failureTextMessage.text = resources.getString(messageError)
        }
    }

    private fun handleScrollingToTopWhenSearching() = lifecycleScope.launch {
        getRefreshLoadStateFlow()
            .simpleScan(count = 2)
            .collectLatest { (previousState, currentState) ->
                if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                    binding.moviesRecyclerView.scrollToPosition(0)
                }
            }
    }

    private fun removeMenuProvider() {
        navigator().targetNavigator {
            it.getToolbar()?.removeMenuProvider(menuProvider)
        }
    }

    private fun getRefreshLoadStateFlow(): Flow<LoadState> {
        return moviesSearchAdapter.loadStateFlow
            .map { it.refresh }
    }

    private fun onDestroyMenu() {
        searchView?.setOnQueryTextListener(null)
        searchItem = null
        searchView = null
        removeMenuProvider()
    }

    override fun onDestroyView() {
        moviesSearchAdapter.removeLoadStateListener(loadStateListener)
        onDestroyMenu()
        super.onDestroyView()
    }

}
