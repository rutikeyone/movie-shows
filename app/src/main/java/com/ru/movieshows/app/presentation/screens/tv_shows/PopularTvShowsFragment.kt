package com.ru.movieshows.app.presentation.screens.tv_shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.ru.movieshows.R
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.presentation.adapters.ItemDecoration
import com.ru.movieshows.app.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.app.presentation.adapters.TryAgainAction
import com.ru.movieshows.app.presentation.adapters.tv_shows.TvShowPaginationAdapter
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.viewmodel.tv_shows.PopularTvShowsViewModel
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.FragmentPopularTvShowsBinding
import com.ru.movieshows.sources.tv_shows.entities.TvShowsEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("DEPRECATED_IDENTITY_EQUALS")
@AndroidEntryPoint
class PopularTvShowsFragment : BaseFragment() {

    @Inject
    lateinit var factory: PopularTvShowsViewModel.Factory
    override val viewModel by viewModelCreator {
        factory.create(
            navigator = navigator()
        )
    }

    private val binding by viewBinding<FragmentPopularTvShowsBinding>()

    private val adapter = TvShowPaginationAdapter(::navigateToTvShowDetails)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_popular_tv_shows, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        collectUiState()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() = with(binding) {
        adapter.addLoadStateListener { loadState -> configureUI(loadState) }
        val itemDecoration = ItemDecoration(8F, resources.displayMetrics)
        val tryAgainAction: TryAgainAction = { adapter.retry() }
        val footerAdapter = LoadStateAdapter(tryAgainAction, requireContext())
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        failurePart.retryButton.setOnClickListener { adapter.retry() }
        tvShowsRecyclerView.layoutManager = gridLayoutManager
        tvShowsRecyclerView.adapter = adapter.withLoadStateFooter(footerAdapter)
        tvShowsRecyclerView.addItemDecoration(itemDecoration)
        tvShowsRecyclerView.itemAnimator = null
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val loadingItem = TvShowPaginationAdapter.LOADING_ITEM
                val isLoadingItem = adapter.getItemViewType(position) === loadingItem
                return if (isLoadingItem) 1 else 3
            }
        }
    }

    private fun configureUI(loadState: CombinedLoadStates) = with(binding) {
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
        val showList = !isListEmpty || loadState.source.refresh is LoadState.NotLoading
        binding.tvShowsRecyclerView.isVisible = showList
        binding.progressBarTvShows.isVisible = loadState.source.refresh is LoadState.Loading
        configureFailurePart(loadState)
    }

    private fun configureFailurePart(loadState: CombinedLoadStates) {
        binding.failurePart.root.isVisible = loadState.source.refresh is LoadState.Error
        if(loadState.source.refresh !is LoadState.Error) return
        val errorState = loadState.refresh as LoadState.Error
        val error = errorState.error as? AppFailure
        with(binding.failurePart) {
            failureTextHeader.text = resources.getString((error?.headerResource() ?: R.string.error_header))
            failureTextMessage.text = resources.getString(error?.errorResource() ?: R.string.an_error_occurred_during_the_operation)
        }
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.popularTvShows.collectLatest { tvShows ->
                adapter.submitData(tvShows)
            }
        }
    }

    private fun navigateToTvShowDetails(tvShow: TvShowsEntity) = viewModel.navigateToTvShowDetails(tvShow)

}