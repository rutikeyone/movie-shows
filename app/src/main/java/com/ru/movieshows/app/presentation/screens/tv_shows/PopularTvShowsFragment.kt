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
import com.ru.movieshows.app.presentation.screens.movies.LoadStateListener
import com.ru.movieshows.app.presentation.viewmodel.tv_shows.PopularTvShowsViewModel
import com.ru.movieshows.app.utils.applyDecoration
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.FragmentPopularTvShowsBinding
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

    private val tvShowPaginationAdapter: TvShowPaginationAdapter by lazy {
        TvShowPaginationAdapter(viewModel)
    }

    private val tvShowSpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val loadingItem = TvShowPaginationAdapter.LOADING_ITEM
            val isLoadingItem = tvShowPaginationAdapter.getItemViewType(position) === loadingItem
            return if (isLoadingItem) 1 else 3
        }
    }

    private val loadStateLoader: LoadStateListener = { state ->
        configureUI(state)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_popular_tv_shows, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        collectUIState()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() = with(binding) {
        val context = requireContext()
        val spanCount = getSpanCount()
        val itemDecoration = ItemDecoration(spanCount = spanCount)
        val tryAgainAction: TryAgainAction = { tvShowPaginationAdapter.retry() }
        val footerAdapter = LoadStateAdapter(context, tryAgainAction)

        val gridLayoutManager = GridLayoutManager(requireContext(), spanCount).apply {
            spanSizeLookup = tvShowSpanSizeLookup
        }

        tvShowPaginationAdapter.addLoadStateListener(loadStateLoader)

        failurePart.retryButton.setOnClickListener { tvShowPaginationAdapter.retry() }

        with(tvShowsRecyclerView) {
            layoutManager = gridLayoutManager
            adapter = tvShowPaginationAdapter.withLoadStateFooter(footerAdapter)
            applyDecoration(itemDecoration)
            itemAnimator = null
        }
    }

    private fun configureUI(loadState: CombinedLoadStates) = with(binding) {
        val isListEmpty = loadState.refresh is LoadState.NotLoading && tvShowPaginationAdapter.itemCount == 0
        val showList = !isListEmpty || loadState.source.refresh is LoadState.NotLoading
        binding.tvShowsRecyclerView.isVisible = showList
        binding.progressBarTvShows.isVisible = loadState.source.refresh is LoadState.Loading
        configureFailurePartUI(loadState)
    }

    private fun configureFailurePartUI(loadState: CombinedLoadStates) {
        binding.failurePart.root.isVisible = loadState.source.refresh is LoadState.Error
        if(loadState.source.refresh !is LoadState.Error) return
        val errorState = loadState.refresh as LoadState.Error
        val error = errorState.error as? AppFailure
        with(binding.failurePart) {
            failureTextHeader.text = resources.getString((error?.headerResource() ?: R.string.error_header))
            failureTextMessage.text = resources.getString(error?.errorResource() ?: R.string.an_error_occurred_during_the_operation)
        }
    }

    private fun collectUIState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.popularTvShows.collectLatest { tvShows ->
                tvShowPaginationAdapter.submitData(tvShows)
            }
        }
    }

    override fun onDestroyView() {
        tvShowPaginationAdapter.removeLoadStateListener(loadStateLoader)
        super.onDestroyView()
    }

}