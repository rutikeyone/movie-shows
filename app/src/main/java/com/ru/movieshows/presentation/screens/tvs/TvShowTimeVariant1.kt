package com.ru.movieshows.presentation.screens.tvs

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentTvShowTimeVariant1Binding
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.utils.viewBinding

private const val TV_SHOWS_ARGS = "tvShowsArgs";
private const val ID = "id"

class TvShowTimeVariant1 : Fragment(R.layout.fragment_tv_show_time_variant1) {
    private val binding by viewBinding<FragmentTvShowTimeVariant1Binding>()

    private var tvShow: TvShowsEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvShow = arguments?.getParcelable(TV_SHOWS_ARGS)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        setupTitle()
        setupTvShowBackground()
        val navController = findNavController()
        val id = tvShow?.id ?: return
        binding.root.setOnClickListener {
            val bundle = Bundle().apply { putString(ID, id) }
            navController.navigate(R.id.action_global_tvShowDetailsFragment, bundle)
        }

    }

    private fun setupTvShowBackground() {
        val backDrop = tvShow?.poster
        if(backDrop == null) {
            binding.tvShowBackground.isVisible = false
            return
        }

        Glide
            .with(this)
            .load(backDrop)
            .centerCrop()
            .into(binding.tvShowBackground)
    }

    private fun setupTitle() {
        val title = tvShow?.name
        if(title == null) {
            binding.tvShowName.isVisible = false
            return
        }
        binding.tvShowName.text = title
    }

    companion object {
        @JvmStatic
        fun newInstance(
            tvShow: TvShowsEntity
        ) =
            TvShowTimeVariant1().apply {
                arguments = Bundle().apply {
                    putParcelable(TV_SHOWS_ARGS, tvShow)
                }
            }
    }
}