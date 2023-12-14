package com.ru.movieshows.presentation.screens.tv_show_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ru.movieshows.R
import com.ru.movieshows.databinding.SeasonModalSheetBinding
import com.ru.movieshows.domain.entity.SeasonEntity
import com.ru.movieshows.presentation.viewmodel.viewBinding
import com.ru.movieshows.presentation.viewmodel.tv_show_details.SeasonDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class SeasonDetailsBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<SeasonDetailsViewModel>()

    private val binding by viewBinding<SeasonModalSheetBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.season_modal_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val season: SeasonEntity? = arguments?.getParcelable(SEASON_ARG)
        val seasonNumber = arguments?.getString(SEASON_NUMBER)
        val seriesId = arguments?.getString(SERIES_ID)
        viewModel.updateData(season, seasonNumber, seriesId)
        viewModel.season.observe(viewLifecycleOwner, ::setupUI)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupUI(season: SeasonEntity?) {
        if(season == null) return
        setupImage(season)
        setupHeader(season)
        setupOverview(season)
        setupAirDate(season)
        setupCountEpisodes(season)
        setupRating(season)
    }

    @SuppressLint("SetTextI18n")
    private fun setupRating(season: SeasonEntity) = with(binding) {
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

    private fun setupCountEpisodes(season: SeasonEntity) = with(binding) {
        val countEpisodes = season.episodeCount
        if(countEpisodes != null) {
            countEpisodesValue.text = countEpisodes.toString()
        } else {
            countEpisodesHeader.isVisible = false
            countEpisodesValue.isVisible = false
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupAirDate(season: SeasonEntity) = with(binding){
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

    private fun setupOverview(season: SeasonEntity) = with(binding) {
        val overview = season.overview
        if(!overview.isNullOrEmpty()) {
            overviewText.text = overview
        } else {
            overviewHeader.isVisible = false
            overviewText.isVisible = false
        }
    }

    private fun setupHeader(season: SeasonEntity) = with(binding) {
        val name = season.name
        if(!name.isNullOrEmpty()) {
            seasonName.text = name
        }
    }

    private fun setupImage(season: SeasonEntity) = with(binding) {
        val photo = season.poster
        if(!photo.isNullOrEmpty()) {
            Glide
                .with(binding.root)
                .load(photo)
                .centerCrop()
                .placeholder(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(poster)
        }
    }

    companion object {
        const val SEASON_ARG = "season_args"
        const val SEASON_NUMBER = "season_number"
        const val SERIES_ID = "series_id"
    }
}
