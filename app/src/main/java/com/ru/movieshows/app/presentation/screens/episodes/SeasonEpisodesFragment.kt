package com.ru.movieshows.app.presentation.screens.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.adapters.ItemDecoration
import com.ru.movieshows.app.presentation.adapters.episode.EpisodesAdapter
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.viewmodel.episode.SeasonEpisodesViewModel
import com.ru.movieshows.app.presentation.viewmodel.episode.state.SeasonEpisodesState
import com.ru.movieshows.app.utils.applyDecoration
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentSeasonEpisodesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine


@AndroidEntryPoint
class SeasonEpisodesFragment : BaseFragment() {

    private val arguments by navArgs<SeasonEpisodesFragmentArgs>()

    @Inject
    lateinit var factory: SeasonEpisodesViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(
            navigator = navigator(),
            arguments = arguments,
        )
    }

    private val binding by viewBinding<FragmentSeasonEpisodesBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.fragment_season_episodes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
    }

    private fun handleState(state: SeasonEpisodesState) {
        binding.root.forEach { it.isVisible = false }
        when (state) {
            SeasonEpisodesState.InPending -> configureInPendingUI()
            is SeasonEpisodesState.Success -> configureSuccessUI(state)
            SeasonEpisodesState.SuccessEmpty -> configureSuccessEmptyUI()
            is SeasonEpisodesState.Failure -> configureFailureUI(state)
        }
    }

    private fun configureSuccessUI(state: SeasonEpisodesState.Success) = with(binding) {
        val context = requireContext()
        val spanCount = getSpanCount()
        val itemDecoration = ItemDecoration(spanCount = spanCount)
        val gridLayoutManager = GridLayoutManager(context, spanCount)
        val episodesAdapter = EpisodesAdapter(state.episodes, viewModel)

        with(episodesRecyclerView) {
            this.adapter = episodesAdapter
            layoutManager = gridLayoutManager
            applyDecoration(itemDecoration)
        }
        successContainer.isVisible = true
    }

    private fun configureSuccessEmptyUI() {
        binding.successEmptyContainer.isVisible = true
    }

    private fun configureFailureUI(state: SeasonEpisodesState.Failure) = with(binding) {
        val header = state.header
        val error = state.error
        failureContainer.isVisible = true
        FailurePartBinding.bind(failurePart.root).also {
            it.retryButton.setOnClickListener { viewModel.tryAgain() }
            if (header != null) it.failureTextHeader.text = resources.getString(header)
            if (error != null) it.failureTextMessage.text = resources.getString(error)
        }
    }

    private fun configureInPendingUI() {
        binding.progressContainer.isVisible = true
    }

}