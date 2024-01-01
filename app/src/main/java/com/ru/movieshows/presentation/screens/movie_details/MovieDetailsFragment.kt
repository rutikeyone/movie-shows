package com.ru.movieshows.presentation.screens.movie_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentMovieDetailsBinding
import com.ru.movieshows.databinding.ReviewItemBinding
import com.ru.movieshows.domain.entity.MovieDetailsEntity
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.entity.ReviewEntity
import com.ru.movieshows.domain.entity.VideoEntity
import com.ru.movieshows.presentation.adapters.InfoAdapter
import com.ru.movieshows.presentation.adapters.ItemDecoration
import com.ru.movieshows.presentation.adapters.MoviesAdapter
import com.ru.movieshows.presentation.adapters.VideosAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.OnTouchListener
import com.ru.movieshows.presentation.utils.extension.clearDecorations
import com.ru.movieshows.presentation.viewmodel.movie_details.MovieDetailsViewModel
import com.ru.movieshows.presentation.viewmodel.movie_details.state.MovieDetailsState
import com.ru.movieshows.presentation.viewmodel.viewBinding
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
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
        viewModel.titleState.observe(viewLifecycleOwner, ::handleTitle)
    }

    private fun handleTitle(value: String?) = navigator().targetNavigator {
        it.getToolbar()?.title = value
    }

    private fun handleUI(state: MovieDetailsState) = with(binding) {
        listOf(progressContainer, failureContainer, successContainer).onEach { it.isVisible = false }
        when (state) {
            MovieDetailsState.InPending -> handleInPendingUI()
            is MovieDetailsState.Failure -> {
                val header = state.header
                val error = state.error
                handleFailureUI(header, error)
            }
            is MovieDetailsState.Success -> {
                val movieDetails = state.movieDetails
                val similarMovies = state.similarMovies
                val reviews = state.reviews
                val videos = state.videos
                handleSuccessUI(movieDetails, similarMovies, reviews, videos)
            }
        }
    }

    private fun handleSuccessUI(
        movie: MovieDetailsEntity,
        similarMovies: ArrayList<MovieEntity>,
        reviews: ArrayList<ReviewEntity>,
        videos: ArrayList<VideoEntity>,
    ) = with(binding){
        successContainer.isVisible = true
        configureBackDrop(movie.backDrop)
        configureMoviePoster(movie)
        configureRating(movie)
        configureRuntime(movie)
        configureReleaseDate(movie)
        configureOverview(movie)
        configureGenres(movie)
        configureCompanies(movie)
        configureSimilarMovies(similarMovies)
        configureVideos(videos)
        configureReview(reviews)
    }

    private fun configureVideos(videos: ArrayList<VideoEntity>) = with(binding) {
        val itemDecorator = ItemDecoration(8F, resources.displayMetrics)
        val adapter = VideosAdapter(::navigateToVideo).also { it.updateData(videos) }
        if (videos.isNotEmpty()) {
            videosRecyclerView.adapter = adapter
            videosRecyclerView.clearDecorations()
            videosRecyclerView.addItemDecoration(itemDecorator)
        } else {
            videosTextView.visibility = View.GONE
            videosRecyclerView.visibility = View.GONE
        }
    }

    private fun configureReview(reviews: ArrayList<ReviewEntity>) {
        val reviewBinding = binding.reviewItem

        with(reviewBinding) {
            if(reviews.isNotEmpty()) {
                val review = reviews.first()
                configureReviewAuthor(this, review)
                configureReviewRating(this, review)
                configureReviewContent(this, review)
                configureReviewAvatar(review, reviewBinding)
                this@MovieDetailsFragment.binding.showAllReviews.setOnClickListener {
                    viewModel.navigateToReviews(reviews)
                }
            } else {
                this@MovieDetailsFragment.binding.reviewsHeader.isVisible = false
                this@MovieDetailsFragment.binding.showAllReviews.isVisible = false
                this.reviewTile.isVisible = false
            }
        }
    }

    private fun configureReviewAvatar(
        review: ReviewEntity,
        reviewBinding: ReviewItemBinding,
    ) = with(reviewBinding.avatarImageView) {
        val avatar = review.authorDetails?.avatar
        if(!avatar.isNullOrEmpty()) {
            Glide
                .with(requireContext())
                .load(avatar)
                .centerCrop()
                .placeholder(R.drawable.poster_placeholder_bg)
                .error(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(reviewBinding.avatarImageView)
        } else {
            Glide
                .with(requireContext())
                .load(R.drawable.poster_placeholder_bg)
                .placeholder(R.drawable.poster_placeholder_bg)
                .error(R.drawable.poster_placeholder_bg)
                .centerCrop()
                .into(reviewBinding.avatarImageView)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun configureReviewContent(
        binding: ReviewItemBinding,
        review: ReviewEntity,
    ) = with(binding) {
        val content = review.content
        with(this.reviewTextView) {
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

    private fun configureReviewAuthor(
        binding: ReviewItemBinding,
        review: ReviewEntity,
    ) = with(binding) {
        val author = review.author
        if (!author.isNullOrEmpty()) {
            this.reviewerHeaderView.text = author
        } else {
            this.reviewerHeaderView.isVisible = false
        }
    }

    private fun configureReviewRating(
        binding: ReviewItemBinding,
        review: ReviewEntity,
    ) = with(binding) {
        val rating = review.authorDetails?.rating?.toFloat()
        if (rating != null && rating > 2) {
            val result = rating / 2
            this.ratingBar.isEnabled = false
            this.ratingBar.rating = result
        } else {
            this.ratingBar.isVisible = false
        }
    }

    private fun configureSimilarMovies(similarMovies: ArrayList<MovieEntity>) {
        val itemDecorator = ItemDecoration(8F, resources.displayMetrics)
        val adapter = MoviesAdapter(::onSimilarMovieTap).also { it.updateData(similarMovies) }
        val headerView = binding.similarMoviesHeader
        val similarMoviesView = binding.similarMovies
        if (similarMovies.isNotEmpty()) {
            similarMoviesView.adapter = adapter
            similarMoviesView.clearDecorations()
            similarMoviesView.addItemDecoration(itemDecorator)
        } else {
            headerView.isVisible = false
            similarMoviesView.isVisible = false
        }
    }

    private fun configureGenres(movieDetails: MovieDetailsEntity) {
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

    private fun configureCompanies(movieDetails: MovieDetailsEntity) {
        val  companies = movieDetails.productionCompanies
        val context = requireContext()
        val headerView = binding.productionCompaniesHeader
        val companiesView = binding.productionCompaniesRecyclerView
        val layoutManager = FlexboxLayoutManager(context).also {
            it.flexDirection = FlexDirection.ROW
            it.justifyContent = JustifyContent.FLEX_START
        }
        if (!companies.isNullOrEmpty()) {
            val names = companies.map { it.name }
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

    private fun configureOverview(movieDetails: MovieDetailsEntity) {
        val overview = movieDetails.overview
        if (!overview.isNullOrEmpty()) {
            binding.overviewText.text = overview
        } else {
            binding.overviewHeader.isVisible = true
            binding.overviewText.isVisible = true
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun configureReleaseDate(movieDetails: MovieDetailsEntity) {
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
    private fun configureRuntime(movieDetails: MovieDetailsEntity) {
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
    private fun configureRating(
        movieDetails: MovieDetailsEntity,
    ) = with(binding){
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

    private fun configureMoviePoster(movieDetails: MovieDetailsEntity) = with(binding.moviePoster) {
        val poster = movieDetails.poster
        if(!poster.isNullOrEmpty()) {
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

    private fun configureBackDrop(backDrop: String?) = with(binding.movieBackDropImageView) {
        if(!backDrop.isNullOrEmpty()) {
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

    private fun handleFailureUI(@StringRes header: Int?, @StringRes error: Int?) {
        FailurePartBinding.bind(binding.failurePart.root).also {
            it.retryButton.setOnClickListener { viewModel.fetchData() }
            if(header != null) it.failureTextHeader.text = resources.getString(header)
            if(error != null) it.failureTextMessage.text = resources.getString(error)
        }
        binding.failureContainer.isVisible = true
    }

    private fun handleInPendingUI() {
        binding.progressContainer.isVisible = true
    }

    private fun onSimilarMovieTap(movieEntity: MovieEntity) = viewModel.navigateToMovieDetails(movieEntity)

    private fun navigateToVideo(video: VideoEntity) = viewModel.navigateToVideo(video)
}