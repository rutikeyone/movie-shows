package com.ru.movieshows.presentation.screens.season_episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentSeasonEpisodesBinding
import com.ru.movieshows.presentation.adapters.EpisodesAdapter
import com.ru.movieshows.presentation.adapters.ItemDecoration
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.extension.clearDecorations
import com.ru.movieshows.presentation.viewmodel.season_episodes.SeasonEpisodesViewModel
import com.ru.movieshows.presentation.viewmodel.season_episodes.state.SeasonEpisodesState
import com.ru.movieshows.presentation.viewmodel.viewBinding
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
        savedInstanceState: Bundle?
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
        val itemDecoration = ItemDecoration(8F, resources.displayMetrics)
        val adapter = EpisodesAdapter(state.episodes) { episode ->
            viewModel.navigateToEpisodeDetails(episode)
        }
        episodesRecyclerView.clearDecorations()
        episodesRecyclerView.addItemDecoration(itemDecoration)
        episodesRecyclerView.adapter = adapter
        successContainer.isVisible = true
    }

    private fun configureSuccessEmptyUI() = with(binding) {
        successEmptyContainer.isVisible = true
    }

    private fun configureFailureUI(state: SeasonEpisodesState.Failure) = with(binding) {
        val header = state.header
        val error = state.error
        failureContainer.isVisible = true
        FailurePartBinding.bind(failurePart.root).also {
            it.retryButton.setOnClickListener { viewModel.tryAgain() }
            if(header != null) it.failureTextHeader.text = resources.getString(header)
            if(error != null) it.failureTextMessage.text = resources.getString(error)
        }
    }

    private fun configureInPendingUI() = with(binding) {
        progressContainer.isVisible = true
    }

}