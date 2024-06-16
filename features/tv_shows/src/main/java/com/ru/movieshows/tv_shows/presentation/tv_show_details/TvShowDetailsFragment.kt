package com.ru.movieshows.tv_shows.presentation.tv_show_details

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
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.core.presentation.applyDecoration
import com.ru.movieshows.core.presentation.args
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.core.presentation.viewModelCreator
import com.ru.movieshows.core.presentation.views.observe
import com.ru.movieshows.navigation.GlobalNavComponentRouter
import com.ru.movieshows.season.presentation.PersonDetailsBottomSheetDialogFragment
import com.ru.movieshows.tv_shows.TvShowsRouter
import com.ru.movieshows.tv_shows.domain.entities.Review
import com.ru.movieshows.tv_shows.domain.entities.Season
import com.ru.movieshows.tv_shows.domain.entities.TvShowDetails
import com.ru.movieshows.tv_shows.domain.entities.Video
import com.ru.movieshows.tv_shows.presentation.views.CreatorAdapter
import com.ru.movieshows.tv_shows.presentation.views.DataAdapter
import com.ru.movieshows.tv_shows.presentation.views.SeasonAdapter
import com.ru.movieshows.tv_shows.presentation.views.VideosAdapter
import com.ru.movieshows.core.presentation.R
import com.ru.movieshows.tvshows.databinding.FragmentTvShowDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class TvShowDetailsFragment : BaseFragment() {

    class Screen(
        val id: String,
    ) : BaseScreen

    @Inject
    lateinit var globalNavComponentRouter: GlobalNavComponentRouter

    @Inject
    lateinit var factory: TvShowDetailsViewModel.Factory

    @Inject
    lateinit var router: TvShowsRouter

    override val viewModel by viewModelCreator {
        factory.create(args())
    }

    private val binding by viewBinding<FragmentTvShowDetailsBinding>()

    private val tvShowSeasonSimpleListener = SimpleAdapterListener<Season> {
        val seriesId = args<Screen>().id
        val seasonNumber = it.seasonNumber?.toString() ?: return@SimpleAdapterListener

        router.launchSeasonDetailsBottomSheetDialog(
            childFragmentManager = childFragmentManager,
            seriesId = seriesId,
            seasonNumber = seasonNumber,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(com.ru.movieshows.tvshows.R.layout.fragment_tv_show_details, container, false)

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

    private fun setupViews(state: TvShowDetailsViewModel.State) {
        val tvShowDetails = state.tvShowDetails
        val reviews = state.reviews
        val videos = state.videos

        setupBackdropImageView(tvShowDetails)
        setupPosterImageView(tvShowDetails)
        setupRatingViews(tvShowDetails)
        setupCountEpisodesViews(tvShowDetails)
        setupReleaseDateViews(tvShowDetails)
        setupOverviewViews(tvShowDetails)
        setupGenresViews(tvShowDetails)
        setupProductionCompaniesViews(tvShowDetails)
        setupCreatedByViews(tvShowDetails)
        setupSeasonsViews(tvShowDetails)
        setupVideosViews(videos)
        setupReviewViews(reviews)
    }

    private fun setupBackdropImageView(tvShowDetails: TvShowDetails) {
        val loadBackDrop = tvShowDetails.backDropPath ?: R.drawable.core_presentation_bg_backdrop_placeholder

        with(binding.tvShowBackDropImageView) {
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

    private fun setupPosterImageView(tvShowDetails: TvShowDetails) {
        val loadPoster = tvShowDetails.posterPath ?: R.drawable.core_presentation_bg_poster_placeholder

        with(binding.tvShowsPosterImageView) {
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

    @SuppressLint("SetTextI18n")
    private fun setupRatingViews(tvShowDetails: TvShowDetails) {
        val rating = tvShowDetails.rating

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

    private fun setupCountEpisodesViews(tvShowDetails: TvShowDetails) {
        with(binding.countEpisodesValueTextView) {
            val countEpisodes = tvShowDetails.numberOfEpisodes ?: return@with
            text = countEpisodes.toString()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupReleaseDateViews(tvShowDetails: TvShowDetails) {
        if (tvShowDetails.firstAirDate != null) {
            val simpleDateFormatter = SimpleDateFormat("d MMMM yyyy")
            val releaseDate = simpleDateFormatter.format(tvShowDetails.firstAirDate)
            binding.releaseDateValueTextView.text = releaseDate
        } else {
            binding.releaseDateHeaderTextView.visibility = View.GONE
            binding.releaseDateValueTextView.visibility = View.GONE
        }
    }

    private fun setupOverviewViews(tvShowDetails: TvShowDetails) {
        with(binding) {
            val overview = tvShowDetails.overview
            val nullOrEmpty = overview.isNullOrEmpty()
            if (!nullOrEmpty) {
                overviewTextView.text = overview
            } else {
                overviewHeaderTextView.visibility = View.GONE
                overviewTextView.visibility = View.GONE
            }
        }
    }

    private fun setupGenresViews(tvShowDetails: TvShowDetails) {
        val genres = tvShowDetails.genres
        if (!genres.isNullOrEmpty()) {
            val names = genres.map { it.name }
            val adapter = DataAdapter(names)

            val layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }

            with(binding.genresRecyclerView) {
                this.layoutManager = layoutManager
                this.adapter = adapter
            }
        } else {
            binding.genresHeaderTextView.visibility = View.GONE
            binding.genresRecyclerView.visibility = View.GONE
        }
    }

    private fun setupProductionCompaniesViews(tvShowDetails: TvShowDetails) {
        val productionCompanies = tvShowDetails.productionCompanies

        if (!productionCompanies.isNullOrEmpty()) {
            val names = productionCompanies.map { it.name }
            val dataAdapter = DataAdapter(names)

            val layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START

            }

            with(binding.productionCompaniesRecyclerView) {
                this.layoutManager = layoutManager
                this.adapter = dataAdapter
            }

        } else {
            binding.productionCompaniesHeaderTextView.visibility = View.GONE
            binding.productionCompaniesRecyclerView.visibility = View.GONE
        }
    }

    private fun setupCreatedByViews(tvShowDetails: TvShowDetails) {
        with(binding) {
            val createdBy = tvShowDetails.createdBy

            if (!createdBy.isNullOrEmpty()) {
                val creatorAdapter = CreatorAdapter(createdBy) {
                    val id = it.id ?: return@CreatorAdapter
                    val personDetailsFragment =
                        PersonDetailsBottomSheetDialogFragment.newInstance(id)
                    personDetailsFragment.show(
                        childFragmentManager,
                        PERSON_DETAILS_MODAL_BOTTOM_SHEET_TAG
                    )
                }

                with(creatorsRecyclerView) {
                    this.adapter = creatorAdapter
                    applyDecoration()
                }

            } else {
                createdByHeaderTextView.isVisible = false
                creatorsRecyclerView.isVisible = false
            }
        }
    }


    private fun setupSeasonsViews(tvShowDetails: TvShowDetails) {
        with(binding) {
            val seasons = tvShowDetails.tvShowSeasons
            if (!seasons.isNullOrEmpty()) {
                val seasonAdapter = SeasonAdapter(seasons, tvShowSeasonSimpleListener)

                with(seasonsRecyclerView) {
                    this.adapter = seasonAdapter
                    applyDecoration()
                }

            } else {
                seasonsHeaderTextView.isVisible = false
                seasonsRecyclerView.isVisible = false
            }
        }
    }

    private fun setupVideosViews(videos: List<Video>) {
        if (videos.isNotEmpty()) {
            val videoSimpleAdapterListener = viewModel.videoSimpleAdapterListener
            val videoAdapter = VideosAdapter(videoSimpleAdapterListener).also {
                it.submitData(videos)
            }

            with(binding.videosRecyclerView) {
                this.adapter = videoAdapter
                applyDecoration()
            }

        } else {
            binding.videosTextView.visibility = View.GONE
            binding.videosRecyclerView.visibility = View.GONE
        }
    }

    private fun setupReviewViews(reviews: List<Review>) {
        if (reviews.isNotEmpty()) {
            val review = reviews.first()

            setupReviewAuthorView(review)
            setupReviewRatingView(review)
            setupReviewContentView(review)
            setupReviewAvatarView(review)

            binding.showAllReviewsButton.setOnClickListener {
                viewModel.launchToTvShowReviews()
            }

        } else {
            binding.reviewsHeaderTextView.isVisible = false
            binding.showAllReviewsButton.isVisible = false
            binding.reviewItem.root.isVisible = false
        }
    }

    private fun setupReviewAuthorView(review: Review) {
        with(binding.reviewItem) {
            val author = review.author

            if (!author.isNullOrEmpty()) {
                this.reviewerHeaderTextView.text = author
            } else {
                this.reviewerHeaderTextView.isVisible = false
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setupReviewContentView(review: Review) {
        with(binding.reviewItem.reviewTextView) {
            val content = review.content
            if (!content.isNullOrEmpty()) {
                val value = Html.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)

                text = value
                isVisible = true
                setOnClickListener {
                    toggle()
                }

            } else {
                isVisible = false
            }
        }
    }

    private fun setupReviewAvatarView(review: Review) {
        with(binding.reviewItem.avatarImageView) {
            val loadAvatarPath = review.authorDetails?.avatarPath

            Glide
                .with(requireContext())
                .load(loadAvatarPath)
                .centerCrop()
                .placeholder(R.drawable.core_presentation_bg_poster_placeholder)
                .error(R.drawable.core_presentation_bg_poster_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        }
    }

    companion object {
        const val PERSON_DETAILS_MODAL_BOTTOM_SHEET_TAG = "PersonDetailsModalBottomSheetTag"
    }

}