package com.ru.movieshows.presentation.screens.movie_videos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentMovieVideosBinding
import com.ru.movieshows.databinding.FragmentMoviesBinding
import com.ru.movieshows.domain.entity.VideoEntity
import com.ru.movieshows.presentation.MainActivity
import com.ru.movieshows.presentation.adapters.VideosAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.screens.movie_reviews.ItemDecoration
import com.ru.movieshows.presentation.screens.tabs.TabsFragmentDirections
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.movie_videos.MovieVideosState
import com.ru.movieshows.presentation.viewmodel.movie_videos.MovieVideosViewModel
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieVideosFragment : BaseFragment(R.layout.fragment_movie_videos) {
    @Inject
    lateinit var factory: MovieVideosViewModel.Factory
    private val args by navArgs<MovieVideosFragmentArgs>()
    override val viewModel by viewModelCreator { factory.create(args.id) }

    private val binding by viewBinding<FragmentMovieVideosBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::renderUI)
    }

    private fun renderUI(state: MovieVideosState) {
        val viewParts = listOf(binding.progressBarView, binding.rvVideosView, binding.failurePart.root)
        viewParts.forEach { it.isVisible = false }
        when(state) {
            MovieVideosState.InPending -> binding.progressBarView.isVisible = true
            is MovieVideosState.Failure -> renderFailureUI(state.header, state.error)
            is MovieVideosState.Success -> renderSuccessUI(state.videos)
            MovieVideosState.SuccessEmpty -> renderSuccessEmptyUI()
        }
    }

    private fun renderSuccessEmptyUI() {
        binding.successEmptyView.isVisible = true
    }

    private fun renderFailureUI(header: Int?, error: Int?) {
        val failurePartBinding = FailurePartBinding.bind(binding.failurePart.root)
        failurePartBinding.root.isVisible = true
        failurePartBinding.retryButton.setOnClickListener { viewModel.fetchVideos() }
        if(header != null) failurePartBinding.failureTextHeader.text = resources.getString(header)
        if(error != null) failurePartBinding.failureTextMessage.text = resources.getString(error)
    }

    private fun renderSuccessUI(videos: ArrayList<VideoEntity>) {
        val adapter = VideosAdapter(videos, ::onVideoTap)
        val itemDecoration = ItemDecoration(resources.displayMetrics)
        binding.rvVideosView.isVisible = true
        binding.rvVideosView.addItemDecoration(itemDecoration)
        binding.rvVideosView.adapter = adapter
    }

    private fun onVideoTap(video: VideoEntity) {
        val route = TabsFragmentDirections.actionTabsFragmentToYoutubeVideoPlayerFragment(video)
        val activity = requireActivity() as MainActivity
        activity.rootNavController.navigate(route)
    }
}