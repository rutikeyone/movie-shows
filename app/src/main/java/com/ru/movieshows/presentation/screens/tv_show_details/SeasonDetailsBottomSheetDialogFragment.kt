package com.ru.movieshows.presentation.screens.tv_show_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ru.movieshows.R
import com.ru.movieshows.databinding.SeasonModalSheetBinding
import com.ru.movieshows.domain.entity.SeasonEntity
import java.text.SimpleDateFormat

class SeasonDetailsBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private var season: SeasonEntity? = null

    private var _binding: SeasonModalSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        season = arguments?.getParcelable(SEASON_ARG)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SeasonModalSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUI()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupUI() {
        setupImage()
        setupHeader()
        setupOverview()
        setupAirDate()
        setupCountEpisodes()
        setupRating()
    }

    @SuppressLint("SetTextI18n")
    private fun setupRating() = with(binding) {
        val rating = season?.rating
        ratingBar.isEnabled = false;
        if (rating != null && rating > 0) {
            ratingText.text = "%.2f".format(rating)
            ratingBar.rating = (rating.toFloat() / 2)
        } else {
            ratingText.isVisible = false
            ratingBar.isVisible = false
        }
    }

    private fun setupCountEpisodes() = with(binding) {
        val countEpisodes = season?.episodeCount
        if(countEpisodes != null) {
            countEpisodesValue.text = countEpisodes.toString()
        } else {
            countEpisodesHeader.isVisible = false
            countEpisodesValue.isVisible = false
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupAirDate() = with(binding){
        val airDate = season?.airDate
        val simpleDateFormatter = SimpleDateFormat("d MMMM yyyy")
        val date = airDate?.let { simpleDateFormatter.format(it) }
        if(date != null) {
            releaseDateValue.text = date
        } else {
            releaseDateHeader.isVisible = false
            releaseDateValue.isVisible = false
        }
    }

    private fun setupOverview() = with(binding) {
        val overview = season?.overview
        if(!overview.isNullOrEmpty()) {
            overviewText.text = overview
        } else {
            overviewHeader.isVisible = false
            overviewText.isVisible = false
        }
    }

    private fun setupHeader() = with(binding) {
        val name = season?.name
        if(!name.isNullOrEmpty()) {
            seasonName.text = name
        }
    }

    private fun setupImage() = with(binding) {
        val photo = season?.poster
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
    }
}
