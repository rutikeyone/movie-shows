package com.ru.movieshows.presentation.screens.episode_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentEpisodeDetailsBinding
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.viewmodel.episode_details.EpisodeDetailsViewModel
import com.ru.movieshows.presentation.viewmodel.episode_details.state.EpisodeDetailsStatus
import com.ru.movieshows.presentation.viewmodel.viewBinding
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EpisodeDetailsFragment : BaseFragment() {

    private val binding by viewBinding<FragmentEpisodeDetailsBinding>()

    private val arguments by navArgs<EpisodeDetailsFragmentArgs>()

    @Inject
    lateinit var factory: EpisodeDetailsViewModel.Factory
    override val viewModel by viewModelCreator {
        factory.create(
            arguments = arguments,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_episode_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { state ->
            configureTitle(state.title)
            handleStatus(state.status)
        }
    }

    private fun handleStatus(status: EpisodeDetailsStatus) {
        binding.root.forEach { it.isVisible = false }
        when (status) {
            EpisodeDetailsStatus.Pure -> {}
            EpisodeDetailsStatus.InPending -> configureInPendingUI()
            is EpisodeDetailsStatus.Success -> {}
            is EpisodeDetailsStatus.Failure -> {}
        }
    }

    private fun configureInPendingUI() {
        binding.progressContainer.isVisible = true
    }

    private fun configureTitle(title: String) = navigator().targetNavigator {
        it.getToolbar()?.title = title
    }

}