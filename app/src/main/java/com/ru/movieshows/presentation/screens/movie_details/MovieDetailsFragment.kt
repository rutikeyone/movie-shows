package com.ru.movieshows.presentation.screens.movie_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.cardview.widget.CardView
import androidx.core.view.children
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.appbar.MaterialToolbar
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentMovieDetailsBinding
import com.ru.movieshows.domain.entity.MovieDetailsEntity
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.entity.ReviewEntity
import com.ru.movieshows.presentation.adapters.GenresAdapter
import com.ru.movieshows.presentation.adapters.MoviesAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.movie_details.MovieDetailsState
import com.ru.movieshows.presentation.viewmodel.movie_details.MovieDetailsViewModel
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment(R.layout.fragment_movie_details) {
    @Inject
    lateinit var factory: MovieDetailsViewModel.Factory
    private val binding by viewBinding<FragmentMovieDetailsBinding>()
    private val args by navArgs<MovieDetailsFragmentArgs>()
    override val viewModel by viewModelCreator { factory.create(args.id) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::renderUI)
        viewModel.title.observe(viewLifecycleOwner, ::renderTitle)
    }

    private fun renderTitle(value: String?) {
        val toolBar = activity?.findViewById<MaterialToolbar>(R.id.tabsToolbar) ?: return
        toolBar.title = value
    }

    private fun renderUI(movieDetailsState: MovieDetailsState) {
        binding.root.children.forEach { it.visibility = View.GONE }
        when (movieDetailsState) {
            MovieDetailsState.InPending -> renderInPendingUI();
            is MovieDetailsState.Failure -> renderFailureUI(movieDetailsState.header, movieDetailsState.error)
            is MovieDetailsState.Success -> renderSuccessUI(
                movieDetailsState.movieDetails,
                movieDetailsState.similarMovies,
                movieDetailsState.reviews
            )
        }
    }

    private fun renderSuccessUI(
        movieDetailsEntity: MovieDetailsEntity,
        similarMovies: ArrayList<MovieEntity>,
        reviews: ArrayList<ReviewEntity>,
    ) {
        binding.successContainer.visibility = View.VISIBLE
        setupMovieBackDrop(movieDetailsEntity)
        setupMoviePoster(movieDetailsEntity)
        setupRating(movieDetailsEntity, movieDetailsEntity.rating)
        setupRuntime(movieDetailsEntity)
        setupReleaseDate(movieDetailsEntity)
        setupOverview(movieDetailsEntity)
        setupGenres(movieDetailsEntity)
        setupSimilarMovies(similarMovies)
        setupReview(reviews)
    }

    private fun setupReview(reviews: ArrayList<ReviewEntity>) {
        val reviewTile = view?.findViewById<CardView>(R.id.reviewTile)
        val authorHeader = reviewTile?.findViewById<TextView>(R.id.reviewerHeaderView)
        val ratingBar = reviewTile?.findViewById<RatingBar>(R.id.ratingBar)
        val reviewTextView = reviewTile?.findViewById<TextView>(R.id.reviewTextView)
        val avatarView = reviewTile?.findViewById<ImageView>(R.id.avatarImageView)

        if(reviews.isNotEmpty()) {
            val review = reviews.first()
            if(review.author != null) authorHeader?.text = review.author
            if(review.authorDetails?.rating != null && review.authorDetails?.rating > 2) {
                val value = review.authorDetails.rating.toFloat() / 2
                ratingBar?.rating = value
            } else {
                ratingBar?.visibility = View.GONE
            }
            if(review.content != null) reviewTextView?.text = review.content
            if(review.authorDetails?.avatar != null && avatarView != null) {
                Glide
                    .with(this)
                    .load(review.authorDetails.avatar)
                    .centerCrop()
                    .into(avatarView)
            } else {
                avatarView?.visibility = View.GONE
            }
        }else {
            binding.reviewsHeader.visibility = View.GONE
            binding.showAllReviews.visibility = View.GONE
            reviewTile?.visibility = View.GONE
        }
    }

    private fun setupSimilarMovies(similarMovies: ArrayList<MovieEntity>) {
        if (similarMovies.isNotEmpty()) {
            val adapter = MoviesAdapter(similarMovies, ::onSimilarMovieTap)
            binding.similarMovies.adapter = adapter
        } else {
            binding.similarMoviesHeader.visibility = View.GONE
            binding.similarMovies.visibility = View.GONE
        }
    }

    private fun onSimilarMovieTap(movieEntity: MovieEntity) = viewModel.navigateToMovieDetails(movieEntity)

    private fun setupGenres(movieDetailsEntity: MovieDetailsEntity) {
        if (movieDetailsEntity.genres.isNotEmpty()) {
            val layoutManager = FlexboxLayoutManager(requireContext())
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            binding.genresView.layoutManager = layoutManager
            val adapter = GenresAdapter(movieDetailsEntity.genres)
            binding.genresView.adapter = adapter
        } else {
            binding.genresHeader.visibility = View.GONE
            binding.genresView.visibility = View.GONE
        }
    }

    private fun setupOverview(movieDetailsEntity: MovieDetailsEntity) {
        if (!movieDetailsEntity.overview.isNullOrEmpty()) {
            binding.overviewText.text = movieDetailsEntity.overview
        } else {
            binding.overviewHeader.visibility = View.GONE
            binding.overviewText.visibility = View.GONE
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupReleaseDate(movieDetailsEntity: MovieDetailsEntity) {
        if (movieDetailsEntity.releaseDate != null) {
            val simpleDateFormatter = SimpleDateFormat("yyyy-MM-dd")
            val date = simpleDateFormatter.format(movieDetailsEntity.releaseDate)
            binding.releaseDateValue.text = date
        } else {
            binding.releaseDateHeader.visibility = View.GONE
            binding.releaseDateValue.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupRuntime(movieDetailsEntity: MovieDetailsEntity) {
        if (movieDetailsEntity.runtime != null) {
            binding.durationValue.text = "${movieDetailsEntity.runtime} ${resources.getString(R.string.min)}"
        } else {
            binding.durationHeader.visibility = View.GONE
            binding.durationValue.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupRating(
        movieDetailsEntity: MovieDetailsEntity,
        rating: Double?,
    ) {
        binding.ratingBar.isEnabled = false;
        if (movieDetailsEntity.rating != null) {
            binding.ratingText.text = "%.2f".format(movieDetailsEntity.rating)
            binding.ratingBar.rating = (rating!!.toFloat() / 2)
        }
    }

    private fun setupMoviePoster(movieDetailsEntity: MovieDetailsEntity) {
        if (movieDetailsEntity.poster != null) {
            Glide
                .with(this)
                .load(movieDetailsEntity.poster)
                .centerCrop()
                .into(binding.moviePoster)
        }
    }

    private fun setupMovieBackDrop(movieDetailsEntity: MovieDetailsEntity) {
        if (movieDetailsEntity.backDrop != null) {
            Glide
                .with(this)
                .load(movieDetailsEntity.backDrop)
                .centerCrop()
                .into(binding.movieBackDrop)
        }
    }

    private fun renderFailureUI(@StringRes header: Int?, @StringRes error: Int?) {
        val failurePartBinding = FailurePartBinding.bind(binding.failurePart.root)
        binding.failureContainer.visibility = View.VISIBLE
        failurePartBinding.retryButton.setOnClickListener { viewModel.fetchData() }
        if(header != null) failurePartBinding.failureTextHeader.text = resources.getString(header)
        if(error != null) failurePartBinding.failureTextMessage.text = resources.getString(error)
    }

    private fun renderInPendingUI() {
        binding.progressContainer.visibility = View.VISIBLE
    }
}