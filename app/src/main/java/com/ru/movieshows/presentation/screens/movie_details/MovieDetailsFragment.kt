package com.ru.movieshows.presentation.screens.movie_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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
import com.ru.movieshows.domain.entity.VideoEntity
import com.ru.movieshows.presentation.adapters.InfoAdapter
import com.ru.movieshows.presentation.adapters.MoviesAdapter
import com.ru.movieshows.presentation.adapters.VideosAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.screens.movie_reviews.ItemDecoration
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.movie_details.MovieDetailsState
import com.ru.movieshows.presentation.viewmodel.movie_details.MovieDetailsViewModel
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject


@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment() {
    private val args by navArgs<MovieDetailsFragmentArgs>()

    @Inject
    lateinit var factory: MovieDetailsViewModel.Factory
    override val viewModel by viewModelCreator { factory.create(args.id) }

    private val binding by viewBinding<FragmentMovieDetailsBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_movie_details, container, false)

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
            val viewParts = listOf(binding.progressContainer, binding.failureContainer, binding.successContainer)
            viewParts.forEach { it.visibility = View.GONE }
            when (movieDetailsState) {
                MovieDetailsState.InPending -> renderInPendingUI();
                is MovieDetailsState.Failure -> renderFailureUI(movieDetailsState.header, movieDetailsState.error)
                is MovieDetailsState.Success -> renderSuccessUI(
                    movieDetailsState.movieDetails,
                    movieDetailsState.similarMovies,
                    movieDetailsState.reviews,
                    movieDetailsState.videos,
                )
            }
        }

        private fun renderSuccessUI(
            movieDetailsEntity: MovieDetailsEntity,
            similarMovies: ArrayList<MovieEntity>,
            reviews: ArrayList<ReviewEntity>,
            videos: ArrayList<VideoEntity>,
        ) {
            binding.successContainer.visibility = View.VISIBLE
            setupMovieBackDrop(movieDetailsEntity)
            setupMoviePoster(movieDetailsEntity)
            setupRating(movieDetailsEntity)
            setupRuntime(movieDetailsEntity)
            setupReleaseDate(movieDetailsEntity)
            setupOverview(movieDetailsEntity)
            setupGenres(movieDetailsEntity)
            setupProductionCompanies(movieDetailsEntity)
            setupSimilarMovies(similarMovies)
            setupVideos(videos)
            setupReview(reviews)
        }

    private fun setupVideos(videos: ArrayList<VideoEntity>) {
        if (videos.isNotEmpty()) {
            val itemDecorator = ItemDecoration(8F, resources.displayMetrics)
            val adapter = VideosAdapter(::navigateToVideo).also { it.updateData(videos) }
            binding.videosRecyclerView.adapter = adapter
            binding.videosRecyclerView.addItemDecoration(itemDecorator)
        } else {
            binding.videosTextView.visibility = View.GONE
            binding.videosRecyclerView.visibility = View.GONE
        }
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
            if(review.authorDetails?.rating != null && review.authorDetails.rating > 2) {
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
                    .placeholder(R.drawable.poster_placeholder_bg)
                    .error(R.drawable.poster_placeholder_bg)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(avatarView)
            } else {
                avatarView?.visibility = View.GONE
            }
            binding.showAllReviews.setOnClickListener {
                viewModel.navigateToReviews(reviews)
            }
        } else {
            binding.reviewsHeader.visibility = View.GONE
            binding.showAllReviews.visibility = View.GONE
            reviewTile?.visibility = View.GONE
        }
    }

    private fun setupSimilarMovies(similarMovies: ArrayList<MovieEntity>) {
        if (similarMovies.isNotEmpty()) {
            val itemDecorator = ItemDecoration(8F, resources.displayMetrics)
            val adapter = MoviesAdapter(::onSimilarMovieTap).also { it.updateData(similarMovies) }
            binding.similarMovies.adapter = adapter
            binding.similarMovies.addItemDecoration(itemDecorator)
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
            val names = movieDetailsEntity.genres.map { it.name }
            val adapter = InfoAdapter(names)
            binding.genresView.adapter = adapter
        } else {
            binding.genresHeader.visibility = View.GONE
            binding.genresView.visibility = View.GONE
        }
    }

    private fun setupProductionCompanies(movieDetailsEntity: MovieDetailsEntity) {
        val  productionCompanies = movieDetailsEntity.productionCompanies
        if (!productionCompanies.isNullOrEmpty()) {
            val layoutManager = FlexboxLayoutManager(requireContext())
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            binding.productionCompaniesRecyclerView.layoutManager = layoutManager
            val names = productionCompanies.map { it.name }
            val adapter = InfoAdapter(names)
            binding.productionCompaniesRecyclerView.adapter = adapter
        } else {
            binding.productionCompaniesHeader.visibility = View.GONE
            binding.productionCompaniesRecyclerView.visibility = View.GONE
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
            val simpleDateFormatter = SimpleDateFormat("d MMMM yyyy")
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
    ) = with(binding){
        ratingBar.isEnabled = false;
        val value = movieDetailsEntity.rating
        if (value != null && value > 0) {
            ratingText.text = "%.2f".format(movieDetailsEntity.rating)
            ratingBar.rating = (value.toFloat() / 2)
        } else {
            ratingText.isVisible = false
            ratingBar.isVisible = false
        }
    }

    private fun setupMoviePoster(movieDetailsEntity: MovieDetailsEntity) {
        if (movieDetailsEntity.poster != null) {
            Glide
                .with(this)
                .load(movieDetailsEntity.poster)
                .centerCrop()
                .placeholder(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.moviePoster)
        }
    }

    private fun setupMovieBackDrop(movieDetailsEntity: MovieDetailsEntity) {
        if (movieDetailsEntity.backDrop != null) {
            Glide
                .with(this)
                .load(movieDetailsEntity.backDrop)
                .centerCrop()
                .placeholder(R.drawable.backdrop_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
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

    private fun navigateToVideo(video: VideoEntity) = viewModel.navigateToVideo(video)
}