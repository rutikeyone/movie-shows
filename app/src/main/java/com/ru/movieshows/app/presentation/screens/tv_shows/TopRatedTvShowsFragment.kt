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
import com.ru.movieshows.app.presentation.viewmodel.tv_shows.TopRatedTvShowsViewModel
import com.ru.movieshows.app.utils.applyDecoration
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.FragmentTopRatedTvShowsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("DEPRECATED_IDENTITY_EQUALS")
@AndroidEntryPoint
class TopRatedTvShowsFragment : BaseFragment() {

    @Inject
    lateinit var factory: TopRatedTvShowsViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(
            navigator = navigator(),
        )
    }

    private val binding by viewBinding<FragmentTopRatedTvShowsBinding>()

    private val tvShowPaginationAdapter: TvShowPaginationAdapter by lazy {
        TvShowPaginationAdapter(viewModel)
    }

    private val tvShowsSpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val loadingItem = TvShowPaginationAdapter.LOADING_ITEM
            val isLoadingItem = tvShowPaginationAdapter.getItemViewType(position) === loadingItem
            return if (isLoadingItem) 1 else 3
        }
    }

    private val loadStateListener: LoadStateListener = { state ->
        configureUI(state)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_top_rated_tv_shows, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        collectState()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() = with(binding) {
        val context = requireContext()
        val spanCount = getSpanCount()
        val itemDecoration = ItemDecoration(spanCount = spanCount)
        val tryAgainAction: TryAgainAction = { tvShowPaginationAdapter.retry() }
        val footerAdapter = LoadStateAdapter(context, tryAgainAction)

        val gridLayoutManager = GridLayoutManager(context, spanCount).apply {
            spanSizeLookup = tvShowsSpanSizeLookup
        }

        tvShowPaginationAdapter.addLoadStateListener(loadStateListener)

        with(binding) {
            failurePart.retryButton.setOnClickListener { tvShowPaginationAdapter.retry() }
            tvShowsRecyclerView.layoutManager = gridLayoutManager
            tvShowsRecyclerView.applyDecoration(itemDecoration)
            tvShowsRecyclerView.adapter = tvShowPaginationAdapter.withLoadStateFooter(footerAdapter)
            tvShowsRecyclerView.itemAnimator = null
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
        val headerError = error?.headerResource() ?: R.string.error_header
        val textError = error?.errorResource() ?: R.string.an_error_occurred_during_the_operation
        with(binding.failurePart) {
            failureTextHeader.text = resources.getString(headerError)
            failureTextMessage.text = resources.getString(textError)
        }
    }

    private fun collectState() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.topRatedTvShows.collectLatest { tvShows ->
            tvShowPaginationAdapter.submitData(tvShows)
        }
    }

    override fun onDestroyView() {
        tvShowPaginationAdapter.removeLoadStateListener(loadStateListener)
        super.onDestroyView()
    }

}