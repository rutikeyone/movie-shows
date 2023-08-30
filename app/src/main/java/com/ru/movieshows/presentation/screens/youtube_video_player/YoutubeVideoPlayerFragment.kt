package com.ru.movieshows.presentation.screens.youtube_video_player

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
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
import com.ru.movieshows.presentation.utils.getYouTubeId
import com.ru.movieshows.presentation.utils.viewBinding


class YoutubeVideoPlayerFragment : Fragment(R.layout.fragment_youtube_video_player) {
    private val binding by viewBinding<FragmentYoutubeVideoPlayerBinding>()

    private val arguments by navArgs<YoutubeVideoPlayerFragmentArgs>()
    private val video get() = arguments.video

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
        override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) = executeOnEnterFullScreen(fullscreenView)
        override fun onExitFullscreen() = executeOnExitFullScreen()
    }

    private fun executeOnEnterFullScreen(fullscreenView: View) {
        isFullscreen = true
        binding.videoImageTile.root.visibility = View.GONE
        binding.toolBar.visibility = View.GONE
        binding.youtubePlayerView.visibility = View.GONE
        binding.fullScreenViewContainer.visibility = View.VISIBLE
        binding.fullScreenViewContainer.addView(fullscreenView)
    }

    private fun executeOnExitFullScreen() {
        isFullscreen = false
        binding.toolBar.visibility = View.VISIBLE
        binding.youtubePlayerView.visibility = View.VISIBLE
        binding.fullScreenViewContainer.visibility = View.GONE
        binding.fullScreenViewContainer.removeAllViews()
    }

    private val mainActivity get() = requireActivity() as MainActivity
    private val rootController get() = mainActivity.rootNavController

    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { _, _, _ -> binding.toolBar.title = video.name ?: ""  }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupYoutubePlayerViewByOrientation(resources.configuration)
        renderView()
        setupYoutubePlayerViewByOrientation(resources.configuration)
        rootController.addOnDestinationChangedListener(destinationChangedListener)
    }

    override fun onDestroyView() {
        rootController.removeOnDestinationChangedListener(destinationChangedListener)
        super.onDestroyView()
    }

    private fun renderView() {
        if(video.key != null) {
            val url: String = resources.getString(R.string.youtube_url) + video.key
            val imageId = url.getYouTubeId()
            val image = resources.getString(R.string.thumbnail_firstPart) + imageId + resources.getString(R.string.thumbnail_secondPart)
            Glide
                .with(this)
                .load(image)
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

    private fun renderToolbar() {
        binding.toolBar.setupWithNavController(rootController)
        binding.toolBar.isTitleCentered = true
        binding.toolBar.title = video.name ?: ""
    }

    override fun onDestroy() {
        youTubePlayer = null
        super.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.toolBar.isVisible = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && !isFullscreen
        setupYoutubePlayerViewByOrientation(newConfig)
    }

    private fun setupYoutubePlayerViewByOrientation(config: Configuration) {
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.youtubePlayerView.matchParent()
        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.youtubePlayerView.wrapContent()
        }
    }
}