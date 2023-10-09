package com.ru.movieshows.presentation.screens.movies

import android.app.ActionBar.LayoutParams
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentMovieTileVariant1Binding
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.utils.Listener
import com.ru.movieshows.presentation.utils.viewBinding

private const val MOVIE_ARG = "movieArg"
private const val LISTENER = "listener";

class MovieTileFragmentVariant1 : Fragment(R.layout.fragment_movie_tile_variant1) {

    private val binding by viewBinding<FragmentMovieTileVariant1Binding>()
    private var movie: MovieEntity? = null
    private var listener: Listener<MovieEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = arguments?.getParcelable(MOVIE_ARG)
        listener = arguments?.getParcelable(LISTENER)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        setupTitle()
        setupMovieImage()

        val value = movie ?: return
        binding.root.setOnClickListener { listener?.onClick(value) }
    }

    private fun setupTitle() {
        val title = movie?.title ?: return
        binding.movieName.text = title
    }

    private fun setupMovieImage() {
        val backDrop = movie?.backDrop ?: return
        Glide
            .with(this)
            .load(backDrop)
            .centerCrop()
            .into(binding.movieBackground)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            movie: MovieEntity,
            listener: Listener<MovieEntity>,
        ) =
            MovieTileFragmentVariant1().apply {
                arguments = Bundle().apply {
                    putParcelable(MOVIE_ARG, movie)
                    putParcelable(LISTENER, listener)
                }
            }
    }
}