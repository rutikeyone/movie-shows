package com.ru.movieshows.app.presentation.screens.video

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.navArgs
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.ru.movieshows.app.databinding.ActivityVideoPlayerBinding
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("SourceLockedOrientationActivity")
@AndroidEntryPoint
class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding

    private val arguments by navArgs<VideoPlayerActivityArgs>()

    private var youTubePlayer: YouTubePlayer? = null

    private var isFullscreen = false

    private val fullScreenListener = object : FullscreenListener {
        override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
            isFullscreen = true
            binding.fullScreenViewContainer.visibility = View.VISIBLE
            binding.fullScreenViewContainer.addView(fullscreenView)

            if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            }
        }

        override fun onExitFullscreen() {
            isFullscreen = false
            binding.fullScreenViewContainer.visibility = View.GONE
            binding.fullScreenViewContainer.removeAllViews()

            if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
    }

    private val youTubePlayerListener = object : AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
            this@VideoPlayerActivity.youTubePlayer = youTubePlayer
            arguments.video.key?.let {
                youTubePlayer.loadOrCueVideo(lifecycle, it, 0f)
            }
            if (this@VideoPlayerActivity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (!isFullscreen) {
                    this@VideoPlayerActivity.youTubePlayer?.toggleFullscreen()
                }
            }
        }

        override fun onStateChange(
            youTubePlayer: YouTubePlayer,
            state: PlayerConstants.PlayerState
        ) = with(binding) {
            if (state == PlayerConstants.PlayerState.BUFFERING) {
                youtubePlayerPlaceholderView.isVisible = false
                youtubePlayerView.isVisible = true
            }
            super.onStateChange(youTubePlayer, state)
        }
    }

    private val iFramePlayerOptions = IFramePlayerOptions.Builder()
        .controls(1)
        .fullscreen(1)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater).also { setContentView(it.root) }
        initYouTubePlayerView()
    }

    override fun onConfigurationChanged(configuration: Configuration) {
        super.onConfigurationChanged(configuration)
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!isFullscreen) {
                youTubePlayer?.toggleFullscreen()
            }
        } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (isFullscreen) {
                youTubePlayer?.toggleFullscreen()
            }
        }
    }

    private fun initYouTubePlayerView() = with(binding) {
        youtubePlayerPlaceholderView.isVisible = true
        youtubePlayerView.isVisible = false
        lifecycle.addObserver(youtubePlayerView)
        youtubePlayerView.addFullscreenListener(fullScreenListener)

        youtubePlayerView.enableAutomaticInitialization = false
        youtubePlayerView.initialize(youTubePlayerListener, iFramePlayerOptions)
    }

}