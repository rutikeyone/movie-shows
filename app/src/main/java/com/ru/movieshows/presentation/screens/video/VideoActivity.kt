package com.ru.movieshows.presentation.screens.video

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.ru.movieshows.R
import com.ru.movieshows.databinding.ActivityVideoBinding
import com.ru.movieshows.domain.entity.CommentEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.adapters.CommentsPaginationAdapter
import com.ru.movieshows.presentation.adapters.LoadStateAdapter
import com.ru.movieshows.presentation.adapters.TryAgainAction
import com.ru.movieshows.presentation.viewmodel.video.VideoViewModel
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding
    private val bottomSheetState get() = BottomSheetBehavior.from(binding.commentBottomSheet.root)

    private val args by navArgs<VideoActivityArgs>()

    @Inject
    lateinit var factory: VideoViewModel.Factory
    val viewModel by viewModelCreator { factory.create(args.video) }

    private var youTubePlayer: YouTubePlayer? = null
    private var isFullscreen = false

    private val commentAdapter = CommentsPaginationAdapter(::showCommentDetailsBottomSheet)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater).also { setContentView(it.root) }
        initImageViewView()
        initCommentView()
        initYouTubePlayerView(binding.youtubePlayerView)
        collectUiState()
    }

    private fun initCommentView() = with(binding) {
        commentAdapter.addLoadStateListener { loadState -> renderUI(loadState) }
        val tryAgainAction: TryAgainAction = { commentAdapter.retry() }
        val footerAdapter = LoadStateAdapter(tryAgainAction, this@VideoActivity)
        commentsRecyclerView.adapter = commentAdapter.withLoadStateFooter(footerAdapter)
        commentsRecyclerView.itemAnimator = null
    }

    private fun showCommentDetailsBottomSheet(commentEntity: CommentEntity) {
        bottomSheetState.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun renderUI(loadState: CombinedLoadStates) = with(binding){
        val isListEmpty = loadState.refresh is LoadState.NotLoading && commentAdapter.itemCount == 0
        val showList = !isListEmpty || loadState.source.refresh is LoadState.NotLoading
        val showEmpty = isListEmpty && loadState.source.refresh is LoadState.NotLoading
        notCommentTextView.isVisible = showEmpty
        commentsRecyclerView.isVisible = showList
        commentsProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
        setupFailurePart(loadState)
    }

    private fun setupFailurePart(loadState: CombinedLoadStates) = with(binding.commentsFailurePart) {
        root.isVisible = loadState.source.refresh is LoadState.Error
        if(loadState.source.refresh !is LoadState.Error) return
        val errorState = loadState.refresh as LoadState.Error
        val error = errorState.error as? AppFailure
        failureTextHeader.text = resources.getString((error?.headerResource() ?: R.string.error_header))
        failureTextMessage.text = resources.getString(error?.errorResource() ?: R.string.an_error_occurred_during_the_operation)
        retryButton.setOnClickListener { commentAdapter.retry() }

    }

    private fun collectUiState() {
        lifecycleScope.launch {
            viewModel.comments.collectLatest { movies ->
                commentAdapter.submitData(movies)
            }
        }
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
        binding.commentBottomSheet.closeImageView.setOnClickListener { bottomSheetState.state = BottomSheetBehavior.STATE_COLLAPSED }
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

                if (this@VideoActivity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (!isFullscreen) {
                        this@VideoActivity.youTubePlayer?.toggleFullscreen()
                    }
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