package com.ru.movieshows.presentation.screens.movie_search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentMovieSearchBinding
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.repository.exceptions.AppFailure
import com.ru.movieshows.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.presentation.adapters.MoviesSearchAdapter
import com.ru.movieshows.presentation.adapters.TryAgainAction
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.screens.movie_reviews.ItemDecoration
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.movie_search.MovieSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieSearchFragment : BaseFragment(R.layout.fragment_movie_search) {
    override val viewModel by viewModels<MovieSearchViewModel>()
    private val toolbar get() = requireActivity().findViewById<MaterialToolbar>(R.id.tabsToolbar)
    private val binding by viewBinding<FragmentMovieSearchBinding>()

    private var adapter: MoviesSearchAdapter? = null
    private var searchView : SearchView? = null
    private var searchItem : MenuItem? = null

    private val handler = Handler(Looper.getMainLooper())

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
           menuInflater.inflate(R.menu.search_view_menu, menu)
           searchItem  = searchItem ?: menu.findItem(R.id.search_action)
           searchView = searchView ?: searchItem?.actionView as SearchView?
           searchView?.maxWidth = Int.MAX_VALUE

           searchView?.setOnQueryTextListener(onQueryTextListener)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
            R.id.search_action -> true
            androidx.appcompat.R.id.search_button -> true
            else -> false
        }
    }

    private val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = true

        override fun onQueryTextChange(newText: String): Boolean {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                viewModel.changeQuery(newText)
            }, 500)
            return true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.addMenuProvider(menuProvider, viewLifecycleOwner)
        if(!viewModel.expanded) {
            searchItem?.expandActionView()
            viewModel.setExpanded(true)
        }
        viewModel.searchMovies.observe(viewLifecycleOwner, ::collectUiState)
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun collectUiState(pagingData: PagingData<MovieEntity>?) = lifecycleScope.launch {
        if (pagingData == null) return@launch
        adapter?.submitData(pagingData)
    }

    private fun initView() {
        val itemDecorator = ItemDecoration(16F, resources.displayMetrics)
        val tryAgainAction: TryAgainAction = { adapter?.refresh() }
        val footerAdapter = LoadStateAdapter(tryAgainAction, requireContext())
        adapter = MoviesSearchAdapter(::navigateToMovieDetails)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.movies.layoutManager = layoutManager
        adapter?.addLoadStateListener { loadState -> renderUi(loadState) }
        binding.failurePart.retryButton.setOnClickListener { adapter?.retry() }
        binding.movies.adapter = adapter!!.withLoadStateFooter(footerAdapter)
        binding.movies.addItemDecoration(itemDecorator)
        binding.movies.itemAnimator = null
    }

    private fun navigateToMovieDetails(movieEntity: MovieEntity) = viewModel.navigateToMovieDetails(movieEntity)

    private fun renderUi(loadState: CombinedLoadStates) {
        val searchMode = viewModel.searchQueryMode
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter?.itemCount == 0
        val showMovies = !isListEmpty && loadState.source.refresh is LoadState.NotLoading && searchMode
        binding.movies.isVisible = showMovies
        binding.progressContainer.isVisible = loadState.source.refresh is LoadState.Loading && searchMode
        binding.successEmptyContainer.isVisible = loadState.source.refresh is LoadState.NotLoading && searchMode && isListEmpty
        setupFailurePart(loadState, searchMode)
    }

    private fun setupFailurePart(loadState: CombinedLoadStates, searchMode: Boolean) {
        binding.failurePart.root.isVisible = loadState.source.refresh is LoadState.Error && searchMode
        if(loadState.source.refresh !is LoadState.Error) return
        val errorState = loadState.refresh as LoadState.Error
        val error = errorState.error as? AppFailure
        binding.failurePart.failureTextHeader.text = resources.getString((error?.headerResource() ?: R.string.error_header))
        binding.failurePart.failureTextMessage.text = resources.getString(error?.errorResource() ?: R.string.an_error_occurred_during_the_operation)
    }

    override fun onDestroyView() {
        toolbar.removeMenuProvider(menuProvider)
        searchItem = null
        searchView = null
        adapter = null
        super.onDestroyView()
    }
}
