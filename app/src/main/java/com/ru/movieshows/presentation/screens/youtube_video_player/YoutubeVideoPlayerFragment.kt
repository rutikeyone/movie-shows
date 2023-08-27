package com.ru.movieshows.presentation.screens.youtube_video_player

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentYoutubeVideoPlayerBinding
import com.ru.movieshows.presentation.MainActivity
import com.ru.movieshows.presentation.screens.tabs.TabsFragment
import com.ru.movieshows.presentation.utils.viewBinding

class YoutubeVideoPlayerFragment : Fragment(R.layout.fragment_youtube_video_player) {
    private val binding by viewBinding<FragmentYoutubeVideoPlayerBinding>()

    private val arguments by navArgs<YoutubeVideoPlayerFragmentArgs>()
    private val video get() = arguments.video
    private val poster get() = arguments.poster

    private var youTubePlayer: YouTubePlayer? = null

    private val youtubePlayerListener = object : AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
            val key = video.key
            this@YoutubeVideoPlayerFragment.youTubePlayer = youTubePlayer
            if(key == null) return
            youTubePlayer.loadVideo(key, 0f)
            binding.videoImageTile.root.isVisible = false
            binding.youtubePlayerView.isVisible = true
        }
    }

    private var isFullscreen = false

    private val fullScreenListener = object : FullscreenListener {
        override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
            isFullscreen = true
            binding.videoImageTile.root.visibility = View.GONE
            binding.toolBar.visibility = View.GONE
            binding.youtubePlayerView.visibility = View.GONE
            binding.fullScreenViewContainer.visibility = View.VISIBLE
            binding.fullScreenViewContainer.addView(fullscreenView)

        }

        override fun onExitFullscreen() {
            isFullscreen = false
            binding.toolBar.visibility = View.VISIBLE
            binding.youtubePlayerView.visibility = View.VISIBLE
            binding.fullScreenViewContainer.visibility = View.GONE
            binding.fullScreenViewContainer.removeAllViews()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderView()
    }

    private fun renderView() {
        if(poster != null) {
            Glide
                .with(this)
                .load(poster)
                .centerCrop()
                .into(binding.videoImageTile.posterImageView)
        }
        renderToolbar()
        setupYoutubePlayerView()
    }

    private fun setupYoutubePlayerView() {
        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1)
            .build()
        binding.youtubePlayerView.enableAutomaticInitialization = false
        binding.youtubePlayerView.initialize(youtubePlayerListener, iFramePlayerOptions)
        binding.youtubePlayerView.addFullscreenListener(fullScreenListener)
        lifecycle.addObserver(binding.youtubePlayerView)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      binding.youtubePlayerView.matchParent();
    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        binding.youtubePlayerView.wrapContent();
    }
    }

    private fun renderToolbar() {
        val mainActivity = requireActivity() as MainActivity
        val rootController = mainActivity.rootNavController
        val appConfiguration = AppBarConfiguration(TabsFragment.tabsTopLevelFragment)
        binding.toolBar.setupWithNavController(rootController, appConfiguration)
        binding.toolBar.isTitleCentered = true
        if(video.name != null) {
            binding.toolBar.title = video.name
        }
    }
}