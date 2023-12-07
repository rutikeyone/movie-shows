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
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentMovieSearchBinding
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.presentation.adapters.MoviesSearchPaginationAdapter
import com.ru.movieshows.presentation.adapters.TryAgainAction
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.screens.movie_reviews.ItemDecoration
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.movie_search.MovieSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieSearchFragment : BaseFragment() {
    private val toolbar: Toolbar? = null
    private val binding by viewBinding<FragmentMovieSearchBinding>()
    private val adapter: MoviesSearchPaginationAdapter = MoviesSearchPaginationAdapter(::navigateToMovieDetails)
    private var searchView : SearchView? = null
    private var searchItem : MenuItem? = null

    override val viewModel by viewModels<MovieSearchViewModel>()

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
        toolbar?.addMenuProvider(menuProvider)
        searchItem?.expandActionView()
        val query = viewModel.queryValue
        if(query != null) {
            searchView?.setQuery(query, false)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onStart()
    }

    override fun onPause() {
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        super.onPause()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchMovies.collectLatest { movies ->
                adapter.submitData(movies)
            }
        }
    }
    private fun initView() {
        adapter.addLoadStateListener(::renderUI)
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

    private fun renderUI(loadState: CombinedLoadStates) = with(binding){
        val searchMode = viewModel.searchQueryMode
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
        val showMovies = !isListEmpty && loadState.source.refresh is LoadState.NotLoading && searchMode
        movies.isVisible = showMovies
        progressContainer.isVisible = loadState.source.refresh is LoadState.Loading && searchMode
        successEmptyContainer.isVisible = loadState.source.refresh is LoadState.NotLoading && searchMode && isListEmpty
        setupFailurePart(loadState, searchMode)
    }

    private fun setupFailurePart(loadState: CombinedLoadStates, searchMode: Boolean) = with(binding) {
        val showFailure = loadState.source.refresh is LoadState.Error && searchMode
        failureContainer.isVisible = showFailure
        if(loadState.source.refresh !is LoadState.Error) return
        val errorState = loadState.refresh as LoadState.Error
        val error = errorState.error as? AppFailure
        failurePart.failureTextHeader.text = resources.getString((error?.headerResource() ?: R.string.error_header))
        failurePart.failureTextMessage.text = resources.getString(error?.errorResource() ?: R.string.an_error_occurred_during_the_operation)
    }

    override fun onDestroyView() {
        searchView?.setOnQueryTextListener(null)
        toolbar?.removeMenuProvider(menuProvider)
        searchItem = null
        searchView = null
        super.onDestroyView()
    }
}
