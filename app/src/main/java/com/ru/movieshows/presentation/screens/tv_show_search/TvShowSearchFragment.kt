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
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentTvShowSearchBinding
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.adapters.ItemDecoration
import com.ru.movieshows.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.presentation.adapters.SearchHintAdapter
import com.ru.movieshows.presentation.adapters.TryAgainAction
import com.ru.movieshows.presentation.adapters.TvShowSearchPaginationAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.viewmodel.tv_show_search.TvShowSearchViewModel
import com.ru.movieshows.presentation.viewmodel.viewBinding
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TvShowSearchFragment : BaseFragment() {

    @Inject
    lateinit var factory: TvShowSearchViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(
            navigator = navigator(),
        )
    }

    private val binding by viewBinding<FragmentTvShowSearchBinding>()
    private val adapter: TvShowSearchPaginationAdapter = TvShowSearchPaginationAdapter(::navigateToTvShowDetails);
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
            searchView?.imeOptions = EditorInfo.IME_ACTION_DONE
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
    ): View = inflater.inflate(R.layout.fragment_tv_show_search, container, false)

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

    private fun initView() {
        adapter.addLoadStateListener(::configureUi)
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

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchTvShows.collectLatest { movies ->
                adapter.submitData(movies)
            }
        }
        collectTrendingTvShowsState()
    }

    private fun collectTrendingTvShowsState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            val isSearchMode = state.isSearchMode
            val trendingTvShows = state.trendingTvShows
            val trendingTvShowsInPending = state.trendingTvShowsInPending
            if (!isSearchMode) {
                binding.maybeYouAreLookingForTextView.isVisible = true
                if(!trendingTvShows.isNullOrEmpty() && !trendingTvShowsInPending) {
                    val names = trendingTvShows.map { it.name }
                    val adapter = SearchHintAdapter(names) {
                        val item = trendingTvShows[it]
                        navigateToTvShowDetails(item)
                    }
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

    private fun navigateToTvShowDetails(tvShows: TvShowsEntity) = viewModel.navigateToTvShowDetails(tvShows)

    private fun configureUi(loadState: CombinedLoadStates) {
        val searchMode = viewModel.isSearchMode
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
        val showTvShows = !isListEmpty && loadState.source.refresh is LoadState.NotLoading && searchMode
        binding.tvShows.isVisible = showTvShows
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
}
