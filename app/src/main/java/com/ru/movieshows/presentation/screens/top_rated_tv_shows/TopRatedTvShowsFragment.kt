package com.ru.movieshows.presentation.screens.top_rated_tv_shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentTopRatedTvShowsBinding
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.presentation.adapters.TryAgainAction
import com.ru.movieshows.presentation.adapters.TvShowListAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.screens.movie_reviews.ItemDecoration
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.top_rated_tv_shows.TopRatedTvShowsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TopRatedTvShowsFragment : BaseFragment() {

    override val viewModel by viewModels<TopRatedTvShowsViewModel>()
    private val binding by viewBinding<FragmentTopRatedTvShowsBinding>()

    private val adapter = TvShowListAdapter(::navigateToTvShowDetails)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_top_rated_tv_shows, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        collectUiState()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() = with(binding) {
        adapter.addLoadStateListener { loadState -> renderUI(loadState) }
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
                return if (adapter!!.getItemViewType(position) === TvShowListAdapter.LOADING_ITEM) 1 else 3
            }
        }
    }

    private fun renderUI(loadState: CombinedLoadStates) = with(binding) {
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
        val showList = !isListEmpty || loadState.source.refresh is LoadState.NotLoading
        binding.tvShowsRecyclerView.isVisible = showList
        binding.progressBarTvShows.isVisible = loadState.source.refresh is LoadState.Loading
        setupFailurePart(loadState)
    }

    private fun setupFailurePart(loadState: CombinedLoadStates) {
        binding.failurePart.root.isVisible = loadState.source.refresh is LoadState.Error
        if(loadState.source.refresh !is LoadState.Error) return
        val errorState = loadState.refresh as LoadState.Error
        val error = errorState.error as? AppFailure
        binding.failurePart.failureTextHeader.text = resources.getString((error?.headerResource() ?: R.string.error_header))
        binding.failurePart.failureTextMessage.text = resources.getString(error?.errorResource() ?: R.string.an_error_occurred_during_the_operation)
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.topRatedTvShows.collectLatest { tvShows ->
                adapter.submitData(tvShows)
            }
        }
    }

    private fun navigateToTvShowDetails(tvShow: TvShowsEntity) = viewModel.navigateToTvShowDetails(tvShow)

}