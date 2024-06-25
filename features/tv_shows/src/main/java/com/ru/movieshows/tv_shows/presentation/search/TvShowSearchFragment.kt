package com.ru.movieshows.tv_shows.presentation.search

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
import com.ru.movieshows.navigation.GlobalNavComponentRouter
import com.ru.movieshows.tv_shows.presentation.adapters.TvShowSearchListAdapter
import com.ru.movieshows.tv_shows.presentation.adapters.TvShowSearchPaginationAdapter
import com.ru.movieshows.tvshows.R
import com.ru.movieshows.tvshows.databinding.FragmentTvShowSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TvShowSearchFragment : BaseFragment() {

    @Inject
    lateinit var globalNavComponentRouter: GlobalNavComponentRouter

    override val viewModel by viewModels<TvShowsSearchViewModel>()

    private val binding by viewBinding<FragmentTvShowSearchBinding>()

    private var searchView: SearchView? = null

    private var searchItem: MenuItem? = null

    private val tvShowSearchPaginationAdapter by lazy {
        TvShowSearchPaginationAdapter(viewModel)
    }

    private val tvShowSearchListAdapter by lazy {
        TvShowSearchListAdapter(viewModel)
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
    ): View = inflater.inflate(R.layout.fragment_tv_show_search, container, false)

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

    private fun initView() {
        val itemDecoration = ItemDecoration(halfPadding = false)
        val tryAgainAction: TryAgainAction = { tvShowSearchPaginationAdapter.retry() }
        val footerAdapter = LoadStateAdapter(requireContext(), tryAgainAction)
        val linearLayoutManager = LinearLayoutManager(requireContext())

        with(binding) {
            root.forEach {
                it.isVisible = false
            }

            failurePart.retryButton.setOnClickListener { tvShowSearchPaginationAdapter.retry() }
        }

        with(binding.tvShowsRecyclerView) {
            layoutManager = linearLayoutManager
            adapter = this@TvShowSearchFragment.tvShowSearchPaginationAdapter.withLoadStateFooter(
                footerAdapter
            )
            applyDecoration(itemDecoration)
            itemAnimator = null
        }

        tvShowSearchPaginationAdapter.addLoadStateListener(loadStateListener)

        with(binding.tvShowsSearchRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@TvShowSearchFragment.tvShowSearchListAdapter
            applyDecoration()
        }

    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.searchTvShows.collect { tvShows ->
                    tvShowSearchPaginationAdapter.submitData(tvShows)
                }
            }

            launch {
                viewModel.tvShowsSearchStateFlow.collect { tvShowsSearch ->
                    tvShowSearchListAdapter.submitList(tvShowsSearch)
                }
            }

        }
    }

    private fun createMenu() {
        addMenuProvider()

        val query = viewModel.queryStateValue
        if (query != null) {
            searchItem?.expandActionView()
            searchView?.setQuery(query, false)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleScrollingToTopWhenSearching() {
        viewLifecycleOwner.lifecycleScope.launch {
            getRefreshLoadStateFlow().simpleScan(count = 2)
                .collectLatest { (previousState, currentState) ->
                    if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                        binding.tvShowsRecyclerView.scrollToPosition(0)
                    }
                }
        }
    }

    private fun addMenuProvider() {
        globalNavComponentRouter.getToolbar()?.addMenuProvider(menuProvider)
    }

    private fun getRefreshLoadStateFlow(): Flow<LoadState> {
        return tvShowSearchPaginationAdapter.loadStateFlow.map { it.refresh }
    }

    private fun setupViewsWhenStateChanged(loadState: CombinedLoadStates) {
        with(binding) {
            val searchMode = viewModel.isSearchMode
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && tvShowSearchPaginationAdapter.itemCount == 0
            val isNotLoading = loadState.source.refresh is LoadState.NotLoading
            val isLoading = loadState.source.refresh is LoadState.Loading
            val showTvShows = !isListEmpty && isNotLoading && searchMode

            successGroup.isVisible = showTvShows
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

    private fun removeMenuProvider() {
        globalNavComponentRouter.getToolbar()?.removeMenuProvider(menuProvider)
    }

    private fun onDestroyMenu() {
        searchView?.setOnQueryTextListener(null)
        searchItem = null
        searchView = null
        removeMenuProvider()
    }

    override fun onDestroyView() {
        tvShowSearchPaginationAdapter.removeLoadStateListener(loadStateListener)
        onDestroyMenu()
        super.onDestroyView()
    }

}