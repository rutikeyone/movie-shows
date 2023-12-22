package com.ru.movieshows.presentation.screens.movie_search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.ru.movieshows.databinding.FragmentMovieSearchBinding
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.adapters.ItemDecoration
import com.ru.movieshows.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.presentation.adapters.MoviesSearchPaginationAdapter
import com.ru.movieshows.presentation.adapters.SearchHintAdapter
import com.ru.movieshows.presentation.adapters.TryAgainAction
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.viewmodel.movie_search.MovieSearchViewModel
import com.ru.movieshows.presentation.viewmodel.viewBinding
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieSearchFragment : BaseFragment() {

    @Inject
    lateinit var factory: MovieSearchViewModel.Factory
    override val viewModel by viewModelCreator {
        factory.create(
            navigator = navigator(),
        )
    }

    private val binding by viewBinding<FragmentMovieSearchBinding>()
    private val adapter: MoviesSearchPaginationAdapter = MoviesSearchPaginationAdapter(::navigateToMovieDetails)
    private var searchView : SearchView? = null
    private var searchItem : MenuItem? = null

    private val handler = Handler(Looper.getMainLooper())

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
           menuInflater.inflate(R.menu.search_view_menu, menu)
           searchItem = menu.findItem(R.id.search_action)
           searchView = searchItem?.actionView as SearchView?
           searchView?.setOnQueryTextListener(onQueryTextListener)
           searchView?.maxWidth = Int.MAX_VALUE
           searchView?.imeOptions = EditorInfo.IME_ACTION_DONE
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
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                viewModel.changeQuery(query)
            }, delay)
            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            val isEmpty = newText.isEmpty()
            val delay = if(isEmpty) 0L else 500
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                viewModel.changeQuery(newText)
            }, delay)
            return true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_movie_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        collectUiState()
        navigator().targetNavigator {
            it.getToolbar()?.addMenuProvider(menuProvider)
        }
        val query = viewModel.queryValue
        if(query != null) {
            searchItem?.expandActionView()
            searchView?.setQuery(query, false)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchMovies.collectLatest { movies ->
                adapter.submitData(movies)
            }
        }
        collectNotPlayingMoviesState()
    }

    private fun collectNotPlayingMoviesState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            val isSearchMode = state.isSearchMode
            val notPlayingMovies = state.notPlayingMovies
            val nowPlayingInPending = state.nowPlayingInPending
            if (!isSearchMode) {
                binding.maybeYouAreLookingForTextView.isVisible = true
                if(!notPlayingMovies.isNullOrEmpty() && !nowPlayingInPending) {
                    val names = notPlayingMovies.map { it.title }
                    val adapter = SearchHintAdapter(names) {
                        val item = notPlayingMovies[it]
                        navigateToMovieDetails(item)
                    }
                    with(binding) {
                        searchHintsRecyclerView.isVisible = true
                        searchHintsProgressBar.isVisible = false
                        searchHintsRecyclerView.adapter = adapter
                    }
                } else if(notPlayingMovies.isNullOrEmpty() && nowPlayingInPending) {
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
    }

    private fun initView() {
        adapter.addLoadStateListener(::handleUI)
        val itemDecorator = ItemDecoration(16F, resources.displayMetrics)
        val tryAgainAction: TryAgainAction = { adapter.retry() }
        val footerAdapter = LoadStateAdapter(tryAgainAction, requireContext())
        val layoutManager = LinearLayoutManager(requireContext())
        binding.movies.layoutManager = layoutManager
        binding.failurePart.retryButton.setOnClickListener { adapter.retry() }
        binding.movies.adapter = adapter.withLoadStateFooter(footerAdapter)
        binding.movies.addItemDecoration(itemDecorator)
        binding.movies.itemAnimator = null
    }

    private fun navigateToMovieDetails(movieEntity: MovieEntity) = viewModel.navigateToMovieDetails(movieEntity)

    private fun handleUI(loadState: CombinedLoadStates) = with(binding){
        val searchMode = viewModel.isSearchMode
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
        val showMovies = !isListEmpty && loadState.source.refresh is LoadState.NotLoading && searchMode
        movies.isVisible = showMovies
        progressContainer.isVisible = loadState.source.refresh is LoadState.Loading && searchMode
        successEmptyContainer.isVisible = loadState.source.refresh is LoadState.NotLoading && searchMode && isListEmpty
        configureFailurePart(loadState, searchMode)
    }

    private fun configureFailurePart(loadState: CombinedLoadStates, searchMode: Boolean) = with(binding) {
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

    override fun onDestroyView() {
        searchView?.setOnQueryTextListener(null)
        navigator().targetNavigator {
            it.getToolbar()?.removeMenuProvider(menuProvider)
        }
        searchItem = null
        searchView = null
        super.onDestroyView()
    }
}
