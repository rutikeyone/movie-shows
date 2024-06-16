package com.ru.movieshows.movies.presentation.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.ru.movieshows.core.presentation.BaseFragment
import com.ru.movieshows.core.presentation.BaseScreen
import com.ru.movieshows.core.presentation.R
import com.ru.movieshows.core.presentation.applyDecoration
import com.ru.movieshows.core.presentation.args
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.core.presentation.viewModelCreator
import com.ru.movieshows.core.presentation.views.observe
import com.ru.movieshows.movies.databinding.FragmentMovieDetailsBinding
import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.domain.entities.MovieDetails
import com.ru.movieshows.movies.domain.entities.Review
import com.ru.movieshows.movies.domain.entities.Video
import com.ru.movieshows.movies.presentation.views.DataAdapter
import com.ru.movieshows.movies.presentation.views.MoviesAdapter
import com.ru.movieshows.movies.presentation.views.VideosAdapter
import com.ru.movieshows.navigation.GlobalNavComponentRouter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject


@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment() {

    class Screen(
        val id: Int,
    ) : BaseScreen

    @Inject
    lateinit var globalNavComponentRouter: GlobalNavComponentRouter

    @Inject
    lateinit var factory: MovieDetailsViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(args())
    }


    private val binding by viewBinding<FragmentMovieDetailsBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        inflater.inflate(com.ru.movieshows.movies.R.layout.fragment_movie_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.root) {
            setTryAgainListener { viewModel.toTryGetMovieDetails() }
            observe(viewLifecycleOwner, viewModel.loadScreenStateLiveValue) { state ->
                setupViews(state)
            }
        }

        viewModel.titleStateLiveValue.observe(viewLifecycleOwner) { title ->
            globalNavComponentRouter.getToolbar()?.let { toolbar ->
                toolbar.title = title
            }
        }

    }

    private fun setupViews(state: MovieDetailsViewModel.State) {
        val movieDetails = state.movieDetails
        val reviews = state.reviews
        val similarMovies = state.similarMovies
        val videos = state.videos

        setupBackdropImageView(movieDetails)
        setupPosterImageView(movieDetails)
        setupRatingViews(movieDetails)
        setupDurationViews(movieDetails)
        setupReleaseViews(movieDetails)
        setupOverviewViews(movieDetails)
        setupGenresViews(movieDetails)
        setupProductionCompaniesViews(movieDetails)
        setupSimilarMoviesViews(similarMovies)
        setupVideosViews(videos)
        setupReviewViews(reviews)
    }

    private fun setupReviewViews(reviews: List<Review>) {
        binding.showAllReviewsButton.setOnClickListener {
            viewModel.launchToReviews()
        }

        if (reviews.isNotEmpty()) {
            val review = reviews.first()

            setupReviewerHeaderView(review)
            setupReviewRatingView(review)
            setupReviewContentView(review)
            setupReviewAvatarView(review)
        } else {
            with(binding) {
                reviewsHeaderTextView.isVisible = false
                showAllReviewsButton.isVisible = false

                binding.reviewItem.root.isVisible = false
            }
        }
    }

    private fun setupReviewAvatarView(review: Review) {
        val authorDetails = review.authorDetails
        val avatarPath = authorDetails?.avatarPath

        with(binding) {
            Glide
                .with(binding.root)
                .load(avatarPath)
                .placeholder(R.drawable.core_presentation_bg_poster_placeholder)
                .error(R.drawable.core_presentation_bg_poster_placeholder)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(reviewItem.avatarImageView)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupReviewContentView(review: Review) {
        val content = review.content
        with(binding.reviewItem.reviewTextView) {
            if (!content.isNullOrEmpty()) {
                val value = Html.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
                text = value
                isVisible = true
                setOnClickListener {
                    this.toggle()
                }
            } else {
                isVisible = false
            }
        }
    }

    private fun setupReviewRatingView(review: Review) {
        with(binding.reviewItem) {
            val rating = review.authorDetails?.rating?.toFloat()
            if (rating != null && rating > 2) {
                val result = rating / 2
                this.ratingBar.isEnabled = false
                this.ratingBar.rating = result
            } else {
                this.ratingBar.isVisible = false
            }
        }
    }

    private fun setupReviewerHeaderView(review: Review) {
        with(binding.reviewItem) {
            val author = review.author
            if (!author.isNullOrEmpty()) {
                this.reviewerHeaderTextView.text = author
            } else {
                this.reviewerHeaderTextView.isVisible = false
            }
        }
    }

    private fun setupVideosViews(videos: List<Video>) {
        val videoSimpleAdapterListener = viewModel.videoSimpleAdapterListener

        val videosAdapter = VideosAdapter(videoSimpleAdapterListener).also {
            it.updateData(videos)
        }

        if (videos.isNotEmpty()) {
            with(binding.videosRecyclerView) {
                this.adapter = videosAdapter
                applyDecoration()
            }
        } else {
            with(binding) {
                videosHeaderTextView.visibility = View.GONE
                videosRecyclerView.visibility = View.GONE
            }
        }

    }

    private fun setupSimilarMoviesViews(similarMovies: List<Movie>) {
        val movieSimpleAdapterListener = viewModel.movieSimpleAdapterListener

        val moviesAdapter = MoviesAdapter(movieSimpleAdapterListener).also {
            it.updateData(similarMovies)
        }

        if (similarMovies.isNotEmpty()) {
            with(binding.similarMoviesRecyclerView) {
                this.adapter = moviesAdapter
                applyDecoration()
            }
        } else {
            with(binding) {
                similarMoviesHeaderTextView.isVisible = false
                similarMoviesRecyclerView.isVisible = false
            }
        }

    }

    private fun setupProductionCompaniesViews(movieDetails: MovieDetails) {
        val productionCompanies = movieDetails.productionCompanies
        val names = productionCompanies?.map { it.name }

        val layoutManager = FlexboxLayoutManager(context).also {
            it.flexDirection = FlexDirection.ROW
            it.justifyContent = JustifyContent.FLEX_START
        }

        if (!names.isNullOrEmpty()) {
            val dataAdapter = DataAdapter(names)

            with(binding.productionCompaniesRecyclerView) {
                this.layoutManager = layoutManager
                this.adapter = dataAdapter
            }

        } else {
            with(binding) {
                productionCompaniesHeaderTextView.visibility = View.GONE
                productionCompaniesRecyclerView.visibility = View.GONE
            }
        }

    }

    private fun setupGenresViews(movieDetails: MovieDetails) {
        val genres = movieDetails.genres
        val names = genres?.map { it.name }

        if (!names.isNullOrEmpty()) {
            val layoutManager = FlexboxLayoutManager(context).also {
                it.flexDirection = FlexDirection.ROW
                it.justifyContent = JustifyContent.FLEX_START
            }

            val dataAdapter = DataAdapter(names)

            with(binding.genresRecyclerView) {
                this.layoutManager = layoutManager
                this.adapter = dataAdapter
            }

        } else {
            binding.genresHeaderTextView.isVisible = false
            binding.genresRecyclerView.isVisible = false
        }

    }

    private fun setupOverviewViews(movieDetails: MovieDetails) {
        val overview = movieDetails.overview
        if (!overview.isNullOrEmpty()) {
            binding.overviewTextView.text = overview
        } else {
            binding.overviewHeaderTextView.isVisible = true
            binding.overviewTextView.isVisible = true
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupReleaseViews(movieDetails: MovieDetails) {
        val releaseDate = movieDetails.releaseDate
        val simpleDateFormatter = SimpleDateFormat("d MMMM yyyy")

        if (releaseDate != null) {
            val date = simpleDateFormatter.format(releaseDate)
            binding.releaseDateValueTextView.text = date
        } else {
            binding.releaseDateHeaderTextView.isVisible = false
            binding.releaseDateValueTextView.isVisible = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupDurationViews(movieDetails: MovieDetails) {
        val runtime = movieDetails.runtime

        with(binding.durationHeaderTextView) {
            isVisible = runtime != null
        }

        with(binding.durationValueTextView) {
            if (runtime != null) {
                text =
                    "${movieDetails.runtime} ${resources.getString(com.ru.movieshows.movies.R.string.min)}"
            } else {
                isVisible = false
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setupRatingViews(movieDetails: MovieDetails) {
        val rating = movieDetails.rating

        with(binding) {
            ratingBar.isEnabled = false
            if (rating != null && rating > 0) {
                val ratingValue = rating.toFloat() / 2
                ratingTextView.text = "%.2f".format(rating)
                ratingBar.rating = ratingValue
            } else {
                ratingTextView.isVisible = false
                ratingBar.isVisible = false
            }
        }
    }

    private fun setupPosterImageView(movieDetails: MovieDetails) {
        val loadPoster =
            movieDetails.posterPath ?: R.drawable.core_presentation_bg_poster_placeholder

        with(binding.moviePosterImageView) {
            Glide
                .with(this)
                .load(loadPoster)
                .centerCrop()
                .placeholder(R.drawable.core_presentation_bg_poster_placeholder)
                .error(R.drawable.core_presentation_bg_poster_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        }
    }

    private fun setupBackdropImageView(movieDetails: MovieDetails) {
        val loadBackDrop =
            movieDetails.backDropPath ?: R.drawable.core_presentation_bg_backdrop_placeholder

        with(binding.movieBackDropImageView) {
            Glide
                .with(this)
                .load(loadBackDrop)
                .centerCrop()
                .placeholder(R.drawable.core_presentation_bg_backdrop_placeholder)
                .error(R.drawable.core_presentation_bg_backdrop_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        }
    }

}