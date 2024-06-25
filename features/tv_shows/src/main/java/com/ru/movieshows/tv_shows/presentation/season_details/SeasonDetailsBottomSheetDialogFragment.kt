package com.ru.movieshows.tv_shows.presentation.season_details

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ru.movieshows.core.presentation.BaseScreen
import com.ru.movieshows.core.presentation.R
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.core.presentation.viewModelCreator
import com.ru.movieshows.core.presentation.views.observe
import com.ru.movieshows.tv_shows.TvShowsRouter
import com.ru.movieshows.tv_shows.domain.entities.Season
import com.ru.movieshows.tvshows.databinding.FragmentSeasonBottomSheetDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class SeasonDetailsBottomSheetDialogFragment : BottomSheetDialogFragment() {

    data class Screen(
        val seasonNumber: String,
        val seriesId: String,
    ) : BaseScreen

    private var expandedHeight: Int? = null

    @Inject
    lateinit var factory: SeasonDetailsViewModel.Factory

    @Inject
    lateinit var router: TvShowsRouter

    private val args: Screen by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(SEASON_ARG, Screen::class.java)
        } else {
            arguments?.getSerializable(SEASON_ARG) as? Screen
        } ?: throw IllegalStateException("The seasonArgument argument must be passed")
    }

    private val binding by viewBinding<FragmentSeasonBottomSheetDialogBinding>()

    private val viewModel by viewModelCreator {
        factory.create(args)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val contextThemeWrapper = ContextThemeWrapper(
            activity,
            com.ru.movieshows.core.theme.R.style.Base_Theme_MovieShows
        )
        return inflater.cloneInContext(contextThemeWrapper)
            .inflate(
                com.ru.movieshows.tvshows.R.layout.fragment_season_bottom_sheet_dialog,
                container,
                false
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val minHeight = resources.getDimensionPixelOffset(com.ru.movieshows.tvshows.R.dimen.dp_240)

        with(binding.root) {
            setTryAgainListener { viewModel.toTryAgain() }
            this.minHeight = minHeight
            this.layoutParams.height = minHeight
        }

        binding.root.observe(viewLifecycleOwner, viewModel.loadScreenStateLiveValue) {
            setupViews(it)
        }

        viewModel.updateSeason()
    }

    private fun setupViews(season: Season) {

        with(binding.root) {
            this.minHeight = ConstraintLayout.LayoutParams.WRAP_CONTENT
            this.layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        }

        setupImageView(season)
        setupSeasonNameView(season)
        setupOverviewView(season)
        setupAirDateView(season)
        setupCountEpisodesView(season)
        setupRatingBarView(season)
        setupEpisodeButtonView(season)
    }

    private fun setupEpisodeButtonView(season: Season) {
        with(binding) {
            val countEpisodes = season.episodeCountValue

            if (countEpisodes != null && countEpisodes > 0) {
                episodesButton.isVisible = true
                episodesButton.setOnClickListener {
                    launchEpisodes()
                }
            } else {
                episodesButton.isVisible = false
            }

        }
    }

    private fun launchEpisodes() {
        router.launchEpisodes(
            seasonNumber = args.seasonNumber,
            seriesId = args.seriesId,
        )
        dismiss()
    }

    @SuppressLint("SetTextI18n")
    private fun setupRatingBarView(season: Season) {
        with(binding) {
            val rating = season.rating

            ratingBar.isEnabled = false;
            if (rating != null && rating > 0) {
                ratingTextView.text = "%.2f".format(rating)
                ratingBar.rating = (rating.toFloat() / 2)
            } else {
                ratingTextView.isVisible = false
                ratingBar.isVisible = false
            }

        }
    }

    private fun setupCountEpisodesView(season: Season) {
        with(binding) {
            val countEpisodes = season.episodeCountValue?.toString()

            if (countEpisodes != null) {
                countEpisodesTextView.text = countEpisodes
            } else {
                countEpisodesHeaderTextView.isVisible = false
                countEpisodesTextView.isVisible = false
            }

        }
    }

    private fun setupImageView(season: Season) {
        val loadPhoto = season.posterPath

        Glide
            .with(this)
            .load(loadPhoto)
            .centerCrop()
            .placeholder(R.drawable.core_presentation_bg_poster_placeholder)
            .error(R.drawable.core_presentation_bg_poster_placeholder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.posterImageView)
    }

    private fun setupSeasonNameView(season: Season) {
        with(binding) {
            val name = season.name
            if (!name.isNullOrEmpty()) {
                seasonNameTextView.text = name
                seasonNameTextView.isVisible = true
            } else {
                seasonNameTextView.isVisible = false
            }
        }
    }

    private fun setupOverviewView(season: Season) {
        with(binding) {
            val overview = season.overview
            if (!overview.isNullOrEmpty()) {
                overviewText.text = overview
            } else {
                overviewHeader.isVisible = false
                overviewText.isVisible = false
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupAirDateView(season: Season) {
        with(binding) {
            val airDate = season.airDate
            val simpleDateFormatter = SimpleDateFormat("d MMMM yyyy")
            val date = airDate?.let { simpleDateFormatter.format(it) }
            if (date != null) {
                releaseDateValue.text = date
            } else {
                releaseDateHeader.isVisible = false
                releaseDateValue.isVisible = false
            }
        }
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowsHeight() * 90 / 100
    }

    @Suppress("DEPRECATION")
    private fun getWindowsHeight(): Int {
        val displayMetrics = DisplayMetrics()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = activity?.display
            display?.getRealMetrics(displayMetrics)
        } else {
            val display = activity?.windowManager?.defaultDisplay
            display?.getMetrics(displayMetrics)
        }

        return displayMetrics.heightPixels
    }

    companion object {

        fun newInstance(
            seasonNumber: String,
            seriesId: String,
        ): SeasonDetailsBottomSheetDialogFragment {
            val arguments = Bundle().also {

                val arguments = Screen(
                    seasonNumber = seasonNumber,
                    seriesId = seriesId,
                )

                it.putSerializable(SEASON_ARG, arguments)
            }
            val fragment = SeasonDetailsBottomSheetDialogFragment().apply {
                this.arguments = arguments
            }
            return fragment
        }

        const val SEASON_ARG = "seasonArgument"

    }

}