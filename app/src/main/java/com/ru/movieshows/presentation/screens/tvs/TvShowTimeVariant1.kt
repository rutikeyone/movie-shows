package com.ru.movieshows.presentation.screens.tvs

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentTvShowTimeVariant1Binding
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.screens.movies.MovieTileFragmentVariant1
import com.ru.movieshows.presentation.utils.Listener
import com.ru.movieshows.presentation.utils.viewBinding

private const val TV_SHOWS_ARGS = "tvShowsArgs";
private const val LISTENER = "listener";

class TvShowTimeVariant1 : Fragment(R.layout.fragment_tv_show_time_variant1) {
    private val binding by viewBinding<FragmentTvShowTimeVariant1Binding>()

    private var tvShow: TvShowsEntity? = null
    private var listener: Listener<TvShowsEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvShow = arguments?.getParcelable(TV_SHOWS_ARGS)
        listener = arguments?.getParcelable(LISTENER)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        setupTitle()
        setupTvShowBackground()
        val tvShow = tvShow ?: return
        binding.root.setOnClickListener { listener?.onClick(tvShow) }
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
            tvShow: TvShowsEntity,
            listener: Listener<TvShowsEntity>,
        ) =
            TvShowTimeVariant1().apply {
                arguments = Bundle().apply {
                    putParcelable(TV_SHOWS_ARGS, tvShow)
                    putParcelable(LISTENER, listener)
                }
            }
    }
}