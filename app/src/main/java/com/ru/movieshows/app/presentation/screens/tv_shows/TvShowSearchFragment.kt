package com.ru.movieshows.app.presentation.screens.tv_shows

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
import com.ru.movieshows.app.presentation.adapters.tv_shows.TvShowSearchPaginationAdapter
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.screens.movies.LoadStateListener
import com.ru.movieshows.app.presentation.viewmodel.tv_shows.TvShowSearchViewModel
import com.ru.movieshows.app.utils.applyDecoration
import com.ru.movieshows.app.utils.simpleScan
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.FragmentTvShowSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class TvShowSearchFragment : BaseFragment() {

    @Inject
    lateinit var factory: TvShowSearchViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(
            navigator = navigator(),
        )
    }

    private val binding by viewBinding<FragmentTvShowSearchBinding>()

    private var searchView: SearchView? = null

    private var searchItem: MenuItem? = null

    private val tvShowSearchAdapter by lazy {
        TvShowSearchPaginationAdapter(viewModel)
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

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when(menuItem.itemId) {
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
    ): View = inflater.inflate(R.layout.fragment_tv_show_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {
        initTvShowsView()
        collectState()
        createMenu()
        handleScrollingToTopWhenSearching()
    }

    private fun createMenu() {
        addMenuProvider()
        val query = viewModel.queryValue
        if(query != null) {
            searchItem?.expandActionView()
            searchView?.setQuery(query, false)
        }
    }

    private fun addMenuProvider() {
        navigator().targetNavigator {
            it.getToolbar()?.addMenuProvider(menuProvider)
        }
    }

    private fun initTvShowsView() {
        val context = requireContext()
        val itemDecoration = ItemDecoration(halfPadding = false)
        val tryAgainAction: TryAgainAction = { tvShowSearchAdapter.refresh() }
        val footerAdapter = LoadStateAdapter(context, tryAgainAction)
        val linearLayoutManager = LinearLayoutManager(context)

        tvShowSearchAdapter.addLoadStateListener(loadStateListener)

        with(binding) {
            failurePart.retryButton.setOnClickListener { tvShowSearchAdapter.retry() }
            tvShowsRecyclerView.layoutManager = linearLayoutManager
            tvShowsRecyclerView.adapter = tvShowSearchAdapter.withLoadStateFooter(footerAdapter)
            tvShowsRecyclerView.applyDecoration(itemDecoration)
            tvShowsRecyclerView.itemAnimator = null
        }
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchTvShows.collectLatest { movies ->
                tvShowSearchAdapter.submitData(movies)
            }
        }
        collectTrendingTvShowsState()
    }

    private fun collectTrendingTvShowsState() = viewModel.state.observe(viewLifecycleOwner) { state ->
        val isSearchMode = state.isSearchMode
        val trendingTvShows = state.trendingTvShows
        val trendingTvShowsInPending = state.trendingTvShowsInPending
        if (!isSearchMode) {
            binding.maybeYouAreLookingForTextView.isVisible = true
            if(!trendingTvShows.isNullOrEmpty() && !trendingTvShowsInPending) {
                val names = trendingTvShows.map { it.name }
                val adapter = SearchHintAdapter(names, viewModel)
                with(binding) {
                    searchHintsRecyclerView.isVisible = true
                    searchHintsProgressBar.isVisible = false
                    searchHintsRecyclerView.adapter = adapter
                }
            } else if(trendingTvShows.isNullOrEmpty() && trendingTvShowsInPending) {
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

    private fun configureUI(loadState: CombinedLoadStates) {
        val searchMode = viewModel.isSearchMode
        val isListEmpty = loadState.refresh is LoadState.NotLoading && tvShowSearchAdapter.itemCount == 0
        val showTvShows = !isListEmpty && loadState.source.refresh is LoadState.NotLoading && searchMode
        binding.tvShowsRecyclerView.isVisible = showTvShows
        binding.progressContainer.isVisible = loadState.source.refresh is LoadState.Loading && searchMode
        binding.successEmptyContainer.isVisible = loadState.source.refresh is LoadState.NotLoading && searchMode && isListEmpty
        configureFailurePart(loadState, searchMode)
    }

    private fun configureFailurePart(loadState: CombinedLoadStates, searchMode: Boolean) {
        val showFailure = loadState.source.refresh is LoadState.Error && searchMode
        binding.failureContainer.isVisible = showFailure
        if(loadState.source.refresh !is LoadState.Error) return
        val errorState = loadState.refresh as LoadState.Error
        val error = errorState.error as? AppFailure
        with(binding.failurePart) {
            failureTextHeader.text = resources.getString((error?.headerResource() ?: R.string.error_header))
            failureTextMessage.text = resources.getString(error?.errorResource() ?: R.string.an_error_occurred_during_the_operation)
        }
    }

    private fun handleScrollingToTopWhenSearching() = lifecycleScope.launch {
        getRefreshLoadStateFlow()
            .simpleScan(count = 2)
            .collectLatest { (previousState, currentState) ->
                if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                    binding.tvShowsRecyclerView.scrollToPosition(0)
                }
            }
    }

    private fun getRefreshLoadStateFlow(): Flow<LoadState> {
        return tvShowSearchAdapter.loadStateFlow
            .map { it.refresh }
    }

    private fun removeMenuProvider() {
        navigator().targetNavigator {
            it.getToolbar()?.removeMenuProvider(menuProvider)
        }
    }

    private fun onDestroyMenu() {
        searchView?.setOnQueryTextListener(null)
        searchItem = null
        searchView = null
        removeMenuProvider()
    }

    override fun onDestroyView() {
        onDestroyMenu()
        super.onDestroyView()
    }

}
