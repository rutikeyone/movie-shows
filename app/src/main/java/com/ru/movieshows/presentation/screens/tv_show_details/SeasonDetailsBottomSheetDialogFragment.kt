package com.ru.movieshows.presentation.screens.tv_show_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ru.movieshows.databinding.SeasonModalSheetBinding
import com.ru.movieshows.domain.entity.SeasonEntity

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
                .into(poster)
        }
    }

    companion object {
        const val SEASON_ARG = "season_args"
    }
}
