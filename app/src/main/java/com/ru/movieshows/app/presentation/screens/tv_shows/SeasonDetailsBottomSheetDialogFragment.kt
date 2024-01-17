package com.ru.movieshows.app.presentation.screens.tv_shows

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.screens.BaseBottomSheetDialogFragment
import com.ru.movieshows.app.presentation.viewmodel.tv_shows.SeasonDetailsViewModel
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.SeasonModalSheetBinding
import com.ru.movieshows.sources.tv_shows.entities.SeasonEntity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SeasonDetailsBottomSheetDialogFragment : BaseBottomSheetDialogFragment() {

    private val initialSeason: SeasonEntity by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(SEASON_ARG, SeasonEntity::class.java)
        } else {
            arguments?.getParcelable(SEASON_ARG)
        } ?: throw IllegalStateException("The season argument must be passed")
    }

    private val seasonNumber: String by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getString(SEASON_NUMBER_ARG)
        } else {
            arguments?.getString(SEASON_NUMBER_ARG)
        } ?: throw IllegalStateException("The seasonNumber argument must be passed")
    }

    private val seriesId: String by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getString(SERIES_ID_ARG)
        } else {
            arguments?.getString(SERIES_ID_ARG)
        } ?: throw IllegalStateException("The seriesId argument must be passed")
    }

    @Inject
    lateinit var factory: SeasonDetailsViewModel.Factory
    val viewModel by viewModelCreator {
        factory.create(
            initialSeason = initialSeason,
            arguments = Pair(seasonNumber, seriesId),
        )
    }

    private val binding by viewBinding<SeasonModalSheetBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.season_modal_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.season.observe(viewLifecycleOwner, ::handleUI)
        viewModel.update()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun handleUI(season: SeasonEntity?) {
        if(season == null) return
        configureImage(season)
        configureHeader(season)
        configureOverview(season)
        configureAirDate(season)
        configureCountEpisodes(season)
        configureRating(season)
        configureEpisodesButton(season)
    }

    private fun configureEpisodesButton(season: SeasonEntity) {
        val countEpisodes = season.episodeCount
        with(binding) {
            if (countEpisodes != null && countEpisodes > 0) {
                episodesButton.isVisible = true
                episodesButton.setOnClickListener {
                    navigateToEpisodes()
                }
            } else {
                episodesButton.isVisible = false
            }
        }
    }

    private fun navigateToEpisodes() {
        parentFragmentManager.setFragmentResult(
            NAVIGATE_TO_EPISODES_CODE,
            bundleOf(
                SERIES_ID_ARG to seriesId,
                SEASON_NUMBER_ARG to seasonNumber
            )
        )
        dismiss()
    }

    @SuppressLint("SetTextI18n")
    private fun configureRating(season: SeasonEntity) = with(binding) {
        val rating = season.rating
        ratingBar.isEnabled = false;
        if (rating != null && rating > 0) {
            ratingText.text = "%.2f".format(rating)
            ratingBar.rating = (rating.toFloat() / 2)
        } else {
            ratingText.isVisible = false
            ratingBar.isVisible = false
        }
    }

    private fun configureCountEpisodes(season: SeasonEntity) = with(binding) {
        val countEpisodes = season.episodeCount
        if(countEpisodes != null) {
            countEpisodesValue.text = countEpisodes.toString()
        } else {
            countEpisodesHeader.isVisible = false
            countEpisodesValue.isVisible = false
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun configureAirDate(season: SeasonEntity) = with(binding){
        val airDate = season.airDate
        val simpleDateFormatter = SimpleDateFormat("d MMMM yyyy")
        val date = airDate?.let { simpleDateFormatter.format(it) }
        if(date != null) {
            releaseDateValue.text = date
        } else {
            releaseDateHeader.isVisible = false
            releaseDateValue.isVisible = false
        }
    }

    private fun configureOverview(season: SeasonEntity) = with(binding) {
        val overview = season.overview
        if(!overview.isNullOrEmpty()) {
            overviewText.text = overview
        } else {
            overviewHeader.isVisible = false
            overviewText.isVisible = false
        }
    }

    private fun configureHeader(season: SeasonEntity) = with(binding) {
        val name = season.name
        if(!name.isNullOrEmpty()) {
            seasonName.text = name
            seasonName.isVisible = true
        } else {
            seasonName.isVisible = false
        }
    }

    private fun configureImage(season: SeasonEntity) = with(binding.poster) {
        val photo = season.poster
        if(!photo.isNullOrEmpty()) {
            Glide
                .with(this)
                .load(photo)
                .centerCrop()
                .placeholder(R.drawable.poster_placeholder_bg)
                .error(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        } else {
            Glide
                .with(context)
                .load(R.drawable.poster_placeholder_bg)
                .placeholder(R.drawable.poster_placeholder_bg)
                .error(R.drawable.poster_placeholder_bg)
                .centerCrop()
                .into(this)
        }
    }

    companion object {

        fun newInstance(
            season: SeasonEntity,
            seasonNumber: String,
            seriesId: String
        ): SeasonDetailsBottomSheetDialogFragment {
            val arguments = Bundle().also {
                it.putParcelable(SEASON_ARG, season)
                it.putString(SEASON_NUMBER_ARG, seasonNumber)
                it.putString(SERIES_ID_ARG, seriesId)
            }
            val fragment = SeasonDetailsBottomSheetDialogFragment().apply {
                this.arguments = arguments
            }
            return fragment
        }

        const val NAVIGATE_TO_EPISODES_CODE = "navigateToEpisodeCode"
        const val SEASON_ARG = "seasonArgument"
        const val SEASON_NUMBER_ARG = "seasonNumberArgument"
        const val SERIES_ID_ARG = "seriesIdArgument"

    }

}