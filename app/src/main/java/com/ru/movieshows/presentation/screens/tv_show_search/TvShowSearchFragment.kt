package com.ru.movieshows.presentation.screens.tv_show_search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import com.ru.movieshows.databinding.FragmentTvShowSearchBinding
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.presentation.adapters.TryAgainAction
import com.ru.movieshows.presentation.adapters.TvShowSearchAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.screens.movie_reviews.ItemDecoration
import com.ru.movieshows.presentation.viewmodel.tv_show_search.TvShowSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TvShowSearchFragment : BaseFragment() {
    override val viewModel by viewModels<TvShowSearchViewModel>()
    private val toolbar get() = activity?.findViewById<MaterialToolbar>(R.id.tabsToolbar)

    private var _binding: FragmentTvShowSearchBinding? = null
    val binding get() = _binding!!

    private val adapter: TvShowSearchAdapter = TvShowSearchAdapter(::navigateToTvShowDetails);
    private var searchView: SearchView? = null
    private var searchItem: MenuItem? = null

    private val handler = Handler(Looper.getMainLooper())

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.search_view_menu, menu)
            searchItem = menu.findItem(R.id.search_action)
            searchView = searchItem?.actionView as SearchView?
            searchView?.setOnQueryTextListener(onQueryTextListener)
            searchView?.maxWidth = Int.MAX_VALUE

        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when(menuItem.itemId) {
            R.id.search_action -> true
            androidx.appcompat.R.id.search_button -> true
            else -> false
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(_binding == null) {
            _binding = FragmentTvShowSearchBinding.inflate(inflater, container, false)
            initView()
        }
        viewModel.searchMovies.observe(viewLifecycleOwner, ::collectUiState)
        adapter.addLoadStateListener(::renderUi)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar?.addMenuProvider(menuProvider)
        searchItem?.expandActionView()
        val query = viewModel.queryValue
        if(query != null) {
            searchView?.setQuery(query, false)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun collectUiState(pagingData: PagingData<TvShowsEntity>?) = lifecycleScope.launch {
        if(pagingData == null) return@launch
        adapter.submitData(pagingData)
    }

    override fun onDestroyView() {
        searchView?.setOnQueryTextListener(null)
        toolbar?.removeMenuProvider(menuProvider)
        searchItem = null
        searchView = null
        _binding = null
        super.onDestroyView()
    }

    private fun navigateToTvShowDetails(tvShows: TvShowsEntity) = viewModel.navigateToTvShowDetails(tvShows)

    private fun renderUi(loadState: CombinedLoadStates) {
        val searchMode = viewModel.searchQueryMode
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
        val showTvShows = !isListEmpty && loadState.source.refresh is LoadState.NotLoading && searchMode
        binding.tvShows.isVisible = showTvShows
        binding.progressContainer.isVisible = loadState.source.refresh is LoadState.Loading && searchMode
        binding.successEmptyContainer.isVisible = loadState.source.refresh is LoadState.NotLoading && searchMode && isListEmpty
        setupFailurePart(loadState, searchMode)
    }

    private fun setupFailurePart(loadState: CombinedLoadStates, searchMode: Boolean) {
        val showFailure = loadState.source.refresh is LoadState.Error && searchMode
        binding.failureContainer.isVisible = showFailure
        if(loadState.source.refresh !is LoadState.Error) return
        val errorState = loadState.refresh as LoadState.Error
        val error = errorState.error as? AppFailure
        binding.failurePart.failureTextHeader.text = resources.getString((error?.headerResource() ?: R.string.error_header))
        binding.failurePart.failureTextMessage.text = resources.getString(error?.errorResource() ?: R.string.an_error_occurred_during_the_operation)
    }

    private fun initView() {
        val itemDecoration = ItemDecoration(16F, resources.displayMetrics)
        val tryAgainAction: TryAgainAction = { adapter.refresh() }
        val footerAdapter = LoadStateAdapter(tryAgainAction, requireContext())
        val layoutManager = LinearLayoutManager(requireContext())
        binding.tvShows.layoutManager = layoutManager
        binding.failurePart.retryButton.setOnClickListener { adapter.retry() }
        binding.tvShows.adapter = adapter.withLoadStateFooter(footerAdapter)
        binding.tvShows.addItemDecoration(itemDecoration)
        binding.tvShows.itemAnimator = null
    }
}