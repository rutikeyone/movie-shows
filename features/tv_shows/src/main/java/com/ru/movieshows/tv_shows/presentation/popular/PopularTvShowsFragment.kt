package com.ru.movieshows.tv_shows.presentation.popular

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.ru.movieshows.core.AppException
import com.ru.movieshows.core.Container
import com.ru.movieshows.core.EmptyException
import com.ru.movieshows.core.presentation.BaseFragment
import com.ru.movieshows.core.presentation.applyDecoration
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.core.presentation.views.ItemDecoration
import com.ru.movieshows.core.presentation.views.LoadStateAdapter
import com.ru.movieshows.tv_shows.presentation.views.TvShowPaginationAdapter
import com.ru.movieshows.tvshows.R
import com.ru.movieshows.tvshows.databinding.FragmentPopularTvShowsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Suppress("DEPRECATED_IDENTITY_EQUALS")
@AndroidEntryPoint
class PopularTvShowsFragment : BaseFragment() {

    override val viewModel by viewModels<PopularTvShowsViewModel>()

    private val binding by viewBinding<FragmentPopularTvShowsBinding>()

    private val tvShowPaginationAdapter by lazy {
        TvShowPaginationAdapter(viewModel)
    }

    private val tvShowSpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val itemViewType = tvShowPaginationAdapter.getItemViewType(position)
            val loadingItem = TvShowPaginationAdapter.LOADING_ITEM
            val isLoadingItem = itemViewType === loadingItem
            return if (isLoadingItem) 1 else 3
        }
    }

    private val loadStateLoader: (CombinedLoadStates) -> Unit = { state ->
        setupViewsWhenStateChanged(state)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_popular_tv_shows, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        tvShowPaginationAdapter.removeLoadStateListener(loadStateLoader)
        super.onDestroyView()
    }

    private fun setupViews() {
        initView()
        collectState()
    }

    private fun initView() {
        val spanCount = getSpanCount()
        val itemDecoration = ItemDecoration(
            spanCount = spanCount
        )

        val tryAgainAction = {
            tvShowPaginationAdapter.retry()
        }

        val footerAdapter = LoadStateAdapter(requireContext(), tryAgainAction)
        val gridLayoutManager = GridLayoutManager(context, spanCount).apply {
            spanSizeLookup = tvShowSpanSizeLookup
        }

        with(tvShowPaginationAdapter) {
            addLoadStateListener(loadStateLoader)
        }

        with(binding.tvShowsRecyclerView) {
            layoutManager = gridLayoutManager
            adapter = tvShowPaginationAdapter.withLoadStateFooter(footerAdapter)
            applyDecoration(itemDecoration)
        }

        binding.root.setTryAgainListener { tvShowPaginationAdapter.retry() }

    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.popularTvShows.collectLatest { movies ->
                tvShowPaginationAdapter.submitData(movies)
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun setupViewsWhenStateChanged(state: CombinedLoadStates) {
        when (val refreshState = state.refresh) {
            is LoadState.Error -> {
                val error = refreshState.error
                val appException = AppException(cause = error)
                val errorContainer = Container.Error(appException)

                binding.root.container = errorContainer
            }

            LoadState.Loading -> {
                binding.root.container = Container.Pending
            }

            is LoadState.NotLoading -> {
                val isListEmpty = tvShowPaginationAdapter.itemCount == 0

                if (isListEmpty) {
                    val emptyException = EmptyException(R.string.the_list_of_tv_shows_is_empty)
                    binding.root.container = Container.Error(emptyException)
                } else {
                    binding.root.container = Container.Success(Unit)
                }

            }
        }
    }

}