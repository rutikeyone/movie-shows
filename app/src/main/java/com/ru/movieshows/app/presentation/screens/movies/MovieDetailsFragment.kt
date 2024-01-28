package com.ru.movieshows.app.presentation.screens.movies

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.adapters.InfoAdapter
import com.ru.movieshows.app.presentation.adapters.VideosAdapter
import com.ru.movieshows.app.presentation.adapters.movies.MoviesAdapter
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.viewmodel.movies.MovieDetailsViewModel
import com.ru.movieshows.app.presentation.viewmodel.movies.state.MovieDetailsState
import com.ru.movieshows.app.utils.OnTouchListener
import com.ru.movieshows.app.utils.applyDecoration
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentMovieDetailsBinding
import com.ru.movieshows.sources.movies.entities.MovieDetailsEntity
import com.ru.movieshows.sources.movies.entities.MovieEntity
import com.ru.movieshows.sources.movies.entities.ReviewEntity
import com.ru.movieshows.sources.movies.entities.VideoEntity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject


@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment() {

    private val args by navArgs<MovieDetailsFragmentArgs>()

    @Inject
    lateinit var factory: MovieDetailsViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(
            navigator = navigator(),
            movieId = args.id,
        )
    }

    private val binding by viewBinding<FragmentMovieDetailsBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_movie_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleUI)
        viewModel.titleState.observe(viewLifecycleOwner, ::handleTitleUI)
    }

    private fun handleTitleUI(value: String?) {
        navigator().targetNavigator {
            it.getToolbar()?.title = value
        }
    }

    private fun handleUI(state: MovieDetailsState) = with(binding) {
        val uiContainers = listOf(progressContainer, failureContainer, successContainer)
        uiContainers.onEach { it.isVisible = false }
        when (state) {
            MovieDetailsState.Empty -> {}
            MovieDetailsState.Pending -> configurePendingUI()
            is MovieDetailsState.Failure -> {
                val header = state.header
                val error = state.error
                configureFailureUI(header, error)
            }
            is MovieDetailsState.Success -> {
                val movieDetails = state.movieDetails
                val similarMovies = state.similarMovies
                val reviews = state.reviews
                val videos = state.videos
                configureSuccessUI(movieDetails, similarMovies, reviews, videos)
            }
        }
    }

    private fun configureSuccessUI(
        movie: MovieDetailsEntity,
        similarMovies: ArrayList<MovieEntity>,
        reviews: ArrayList<ReviewEntity>,
        videos: ArrayList<VideoEntity>,
    ) {
        binding.successContainer.isVisible = true
        configureBackDropUI(movie.backDrop)
        configureMoviePosterUI(movie)
        configureRatingUI(movie)
        configureRuntimeUI(movie)
        configureReleaseDateUI(movie)
        configureOverviewUI(movie)
        configureGenresUI(movie)
        configureCompaniesUI(movie)
        configureSimilarMoviesUI(similarMovies)
        configureVideosUI(videos)
        configureReviewUI(reviews)
    }

    private fun configureVideosUI(videos: ArrayList<VideoEntity>) = with(binding) {
        val adapter = VideosAdapter(viewModel.videoSimpleAdapterListener).also {
            it.updateData(videos)
        }
        if (videos.isNotEmpty()) {
            videosRecyclerView.adapter = adapter
            videosRecyclerView.applyDecoration()
        } else {
            videosTextView.visibility = View.GONE
            videosRecyclerView.visibility = View.GONE
        }
    }

    private fun configureReviewUI(reviews: ArrayList<ReviewEntity>) {
        if(reviews.isNotEmpty()) {
            val review = reviews.first()
            configureReviewAuthorUI(review)
            configureReviewRatingUI(review)
            configureReviewContentUI(review)
            configureReviewAvatarUI(review)
            this@MovieDetailsFragment.binding.showAllReviews.setOnClickListener {
                viewModel.navigateToReviews(reviews)
            }
        } else {
            this@MovieDetailsFragment.binding.reviewsHeader.isVisible = false
            this@MovieDetailsFragment.binding.showAllReviews.isVisible = false
            binding.reviewItem.reviewTile.isVisible = false
        }
    }

    private fun configureReviewAvatarUI(review: ReviewEntity) {
        val avatar = review.authorDetails?.avatar
        if (!avatar.isNullOrEmpty()) {
            Glide
                .with(requireContext())
                .load(avatar)
                .centerCrop()
                .placeholder(R.drawable.poster_placeholder_bg)
                .error(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.reviewItem.avatarImageView)
        } else {
            Glide
                .with(requireContext())
                .load(R.drawable.poster_placeholder_bg)
                .placeholder(R.drawable.poster_placeholder_bg)
                .error(R.drawable.poster_placeholder_bg)
                .centerCrop()
                .into(binding.reviewItem.avatarImageView)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun configureReviewContentUI(review: ReviewEntity) {
        val content = review.content
        with(binding.reviewItem.reviewTextView) {
            if (!content.isNullOrEmpty()) {
                val value = Html.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
                val onTouchListener = OnTouchListener()
                text = value
                isVisible = true
                setOnTouchListener(onTouchListener)
            } else {
                isVisible = false
            }
        }
    }

    private fun configureReviewAuthorUI(review: ReviewEntity) = with(binding.reviewItem) {
        val author = review.author
        if (!author.isNullOrEmpty()) {
            this.reviewerHeaderView.text = author
        } else {
            this.reviewerHeaderView.isVisible = false
        }
    }

    private fun configureReviewRatingUI(review: ReviewEntity) = with(binding.reviewItem) {
        val rating = review.authorDetails?.rating?.toFloat()
        if (rating != null && rating > 2) {
            val result = rating / 2
            this.ratingBar.isEnabled = false
            this.ratingBar.rating = result
        } else {
            this.ratingBar.isVisible = false
        }
    }

    private fun configureSimilarMoviesUI(similarMovies: ArrayList<MovieEntity>) {
        val adapter = MoviesAdapter(viewModel.movieSimpleAdapterListener).also {
            it.updateData(similarMovies)
        }
        val headerView = binding.similarMoviesHeader
        val similarMoviesView = binding.similarMovies
        if (similarMovies.isNotEmpty()) {
            with(similarMoviesView) {
                this.adapter = adapter
                applyDecoration()
            }
        } else {
            headerView.isVisible = false
            similarMoviesView.isVisible = false
        }
    }

    private fun configureGenresUI(movieDetails: MovieDetailsEntity) {
        val genres = movieDetails.genres
        val context = requireContext()
        val layoutManager = FlexboxLayoutManager(context).also {
            it.flexDirection = FlexDirection.ROW
            it.justifyContent = JustifyContent.FLEX_START
        }
        val names = movieDetails.genres.map { it.name }
        val adapter = InfoAdapter(names)
        if (genres.isNotEmpty()) {
            with(binding.genresView) {
                this.layoutManager = layoutManager
                this.adapter = adapter
            }
        } else {
            binding.genresHeader.isVisible = false
            binding.genresView.isVisible = false
        }
    }

    private fun configureCompaniesUI(movieDetails: MovieDetailsEntity) {
        val productionCompanies = movieDetails.productionCompanies
        val context = requireContext()
        val headerView = binding.productionCompaniesHeader
        val companiesView = binding.productionCompaniesRecyclerView
        val layoutManager = FlexboxLayoutManager(context).also {
            it.flexDirection = FlexDirection.ROW
            it.justifyContent = JustifyContent.FLEX_START
        }
        if (!productionCompanies.isNullOrEmpty()) {
            val names = productionCompanies.map { it.name }
            val adapter = InfoAdapter(names)
            with(companiesView) {
                this.layoutManager = layoutManager
                this.adapter = adapter
            }
        } else {
            headerView.visibility = View.GONE
            companiesView.visibility = View.GONE
        }
    }

    private fun configureOverviewUI(movieDetails: MovieDetailsEntity) {
        val overview = movieDetails.overview
        if (!overview.isNullOrEmpty()) {
            binding.overviewText.text = overview
        } else {
            binding.overviewHeader.isVisible = true
            binding.overviewText.isVisible = true
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun configureReleaseDateUI(movieDetails: MovieDetailsEntity) {
        val releaseDate = movieDetails.releaseDate
        val simpleDateFormatter = SimpleDateFormat("d MMMM yyyy")
        if (releaseDate != null) {
            val date = simpleDateFormatter.format(releaseDate)
            binding.releaseDateValue.text = date
        } else {
            binding.releaseDateHeader.isVisible = false
            binding.releaseDateValue.isVisible = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun configureRuntimeUI(movieDetails: MovieDetailsEntity) {
        val runtime = movieDetails.runtime
        val durationValue = binding.durationValue
        val durationHeader = binding.durationHeader
        if (runtime != null) {
            durationValue.text = "${movieDetails.runtime} ${resources.getString(R.string.min)}"
        } else {
            durationHeader.isVisible = false
            durationValue.isVisible = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun configureRatingUI(movieDetails: MovieDetailsEntity) = with(binding) {
        val rating = movieDetails.rating
        ratingBar.isEnabled = false
        if (rating != null && rating > 0) {
            ratingText.text = "%.2f".format(rating)
            ratingBar.rating = (rating.toFloat() / 2)
        } else {
            ratingText.isVisible = false
            ratingBar.isVisible = false
        }
    }

    private fun configureMoviePosterUI(movieDetails: MovieDetailsEntity) = with(binding.moviePosterImageView) {
        val poster = movieDetails.poster
        if (!poster.isNullOrEmpty()) {
            Glide
                .with(this)
                .load(poster)
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

    private fun configureBackDropUI(backDrop: String?) = with(binding.movieBackDropImageView) {
        if (!backDrop.isNullOrEmpty()) {
            Glide
                .with(this@MovieDetailsFragment)
                .load(backDrop)
                .centerCrop()
                .placeholder(R.drawable.backdrop_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        } else {
            Glide
                .with(context)
                .load(R.drawable.backdrop_placeholder_bg)
                .placeholder(R.drawable.backdrop_placeholder_bg)
                .error(R.drawable.backdrop_placeholder_bg)
                .centerCrop()
                .into(this)
        }
    }

    private fun configureFailureUI(@StringRes header: Int?, @StringRes error: Int?) {
        FailurePartBinding.bind(binding.failurePart.root).also {
            it.retryButton.setOnClickListener { viewModel.fetchData() }
            if(header != null) it.failureTextHeader.text = resources.getString(header)
            if(error != null) it.failureTextMessage.text = resources.getString(error)
        }
        binding.failureContainer.isVisible = true
    }

    private fun configurePendingUI() {
        binding.progressContainer.isVisible = true
    }

}