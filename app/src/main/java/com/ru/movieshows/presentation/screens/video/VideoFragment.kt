package com.ru.movieshows.presentation.screens.video

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentVideoBinding
import com.ru.movieshows.presentation.contract.navigator
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.video.VideoViewModel
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VideoFragment : BaseFragment() {
    private val args by navArgs<VideoFragmentArgs>()
    private val binding by viewBinding<FragmentVideoBinding>()

    @Inject
    lateinit var factory: VideoViewModel.Factory
    override val viewModel by viewModelCreator { factory.create(args.video) }

    private var youTubePlayer: YouTubePlayer? = null
    private var isFullscreen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_video, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initImageViewView()
        initYouTubePlayerView(binding.youtubePlayerView)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initImageViewView() = with(binding.videoImageView) {
        viewModel.video.image?.let { image ->
            Glide
                .with(this@VideoFragment)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this.posterImageView)
        }
    }

    private fun initYouTubePlayerView(youTubePlayerView: YouTubePlayerView) {
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true
                binding.fullScreenViewContainer.visibility = View.VISIBLE
                binding.fullScreenViewContainer.addView(fullscreenView)

                if (requireActivity().requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                }
            }

            override fun onExitFullscreen() {
                isFullscreen = false
                navigator().getBottomNavigationView()?.visibility = View.VISIBLE
                binding.fullScreenViewContainer.visibility = View.GONE
                binding.fullScreenViewContainer.removeAllViews()

                if (requireActivity().requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
                    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
                    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
                }
            }
        })

        val youTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                binding.videoImageView.root.visibility = View.GONE
                this@VideoFragment.youTubePlayer = youTubePlayer
                viewModel.video.key?.let {
                    youTubePlayer.loadOrCueVideo(lifecycle, it, 0f)
                }
            }

            override fun onPlaybackRateChange(youTubePlayer: YouTubePlayer, playbackRate: PlayerConstants.PlaybackRate) {}
        }

        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1)
            .build()

        youTubePlayerView.enableAutomaticInitialization = false
        youTubePlayerView.initialize(youTubePlayerListener, iFramePlayerOptions)
    }
}