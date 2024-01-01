package com.ru.movieshows.presentation.screens.tv_show_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.SeasonModalSheetBinding
import com.ru.movieshows.domain.entity.SeasonEntity
import com.ru.movieshows.presentation.screens.BaseBottomSheetDialogFragment
import com.ru.movieshows.presentation.viewmodel.tv_show_details.SeasonDetailsViewModel
import com.ru.movieshows.presentation.viewmodel.viewBinding
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class SeasonDetailsBottomSheetDialogFragment : BaseBottomSheetDialogFragment() {

    private val arguments by navArgs<SeasonDetailsBottomSheetDialogFragmentArgs>()

    @Inject
    lateinit var factory: SeasonDetailsViewModel.Factory
    val viewModel by viewModelCreator {
        val season = arguments.seasonArgs
        val seasonNumber = arguments.seasonNumber
        val seriesId = arguments.seriesId
        factory.create(
            initialSeason = season,
            arguments = Pair(seasonNumber, seriesId),
            navigator = navigator(),
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
        viewModel.updateData()
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
                    viewModel.navigateToEpisodes()
                }
            } else {
                episodesButton.isVisible = false
            }
        }
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
        const val SEASON_ARG = "season_args"
        const val SEASON_NUMBER = "season_number"
        const val SERIES_ID = "series_id"
    }
}
