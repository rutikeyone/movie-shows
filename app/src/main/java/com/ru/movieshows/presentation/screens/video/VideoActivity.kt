package com.ru.movieshows.presentation.screens.video

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
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
import com.ru.movieshows.databinding.ActivityVideoBinding
import com.ru.movieshows.presentation.viewmodel.video.VideoViewModel
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding

    private val args by navArgs<VideoActivityArgs>()

    @Inject
    lateinit var factory: VideoViewModel.Factory
    val viewModel by viewModelCreator { factory.create(args.video) }

    private var youTubePlayer: YouTubePlayer? = null
    private var isFullscreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater).also { setContentView(it.root) }
        initImageViewView()
        initYouTubePlayerView(binding.youtubePlayerView)
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

    override fun onBackPressed() {
        if (isFullscreen) {
            youTubePlayer?.toggleFullscreen()
        } else {
            super.onBackPressed()
        }
    }

    private fun initImageViewView() = with(binding.videoImageView) {
        viewModel.video.image?.let { image ->
            Glide
                .with(this@VideoActivity)
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

                if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                }
            }

            override fun onExitFullscreen() {
                isFullscreen = false
                binding.fullScreenViewContainer.visibility = View.GONE
                binding.fullScreenViewContainer.removeAllViews()

                if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
                }
            }
        })

        val youTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                binding.videoImageView.root.visibility = View.GONE
                this@VideoActivity.youTubePlayer = youTubePlayer
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