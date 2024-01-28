package com.ru.movieshows.app.presentation.screens.tv_shows

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.adapters.InfoAdapter
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.app.presentation.adapters.VideosAdapter
import com.ru.movieshows.app.presentation.adapters.tv_shows.CreatorAdapter
import com.ru.movieshows.app.presentation.adapters.tv_shows.SeasonAdapter
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.viewmodel.tv_shows.TvShowDetailsViewModel
import com.ru.movieshows.app.presentation.viewmodel.tv_shows.state.TvShowDetailsState
import com.ru.movieshows.app.utils.OnTouchListener
import com.ru.movieshows.app.utils.applyDecoration
import com.ru.movieshows.app.utils.observeEvent
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentTvShowDetailsBinding
import com.ru.movieshows.sources.movies.entities.ReviewEntity
import com.ru.movieshows.sources.movies.entities.VideoEntity
import com.ru.movieshows.sources.people.entities.PersonEntity
import com.ru.movieshows.sources.tv_shows.entities.CreatorEntity
import com.ru.movieshows.sources.tv_shows.entities.SeasonEntity
import com.ru.movieshows.sources.tv_shows.entities.TvShowDetailsEntity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class TvShowDetailsFragment : BaseFragment() {

    private val args by navArgs<TvShowDetailsFragmentArgs>()

    private val binding by viewBinding<FragmentTvShowDetailsBinding>()

    @Inject
    lateinit var factory: TvShowDetailsViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(
            tvShowId = args.id,
            navigator = navigator(),
            toasts = toasts(),
            resources = resources(),
        )
    }

    private val personSimpleListener = object : SimpleAdapterListener<CreatorEntity> {
        override fun onClickItem(data: CreatorEntity) {
            val id = data.id ?: return
            viewModel.getPersonDetails(id)
        }
    }

    private val seasonSimpleListener = object : SimpleAdapterListener<SeasonEntity> {
        override fun onClickItem(data: SeasonEntity) {
            showSeasonModalBottomSheet(data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_tv_show_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.titleState.observe(viewLifecycleOwner, ::handleTitleState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        viewModel.showPeopleDetailsEvent.observeEvent(viewLifecycleOwner, ::showPeopleDetailsModalBottomSheet)
    }

    private fun showPeopleDetailsModalBottomSheet(person: PersonEntity) {
        val fragment = PersonDetailsBottomSheetDialogFragment.newInstance(person)
        fragment.show(childFragmentManager, PERSON_DETAILS_MODAL_BOTTOM_SHEET_TAG)
    }

    private fun handleState(state: TvShowDetailsState) = with(binding) {
        val viewParts = listOf(successContainer, failureContainer, progressContainer)
        viewParts.forEach { it.isVisible = false }
        when(state) {
            TvShowDetailsState.Empty -> {}
            TvShowDetailsState.InPending -> configureInPendingUI()
            is TvShowDetailsState.Failure -> configureFailureUI(state)
            is TvShowDetailsState.Success -> configureSuccessUI(state)
        }
    }

    private fun configureSuccessUI(state: TvShowDetailsState.Success) = with(binding.successContainer) {
        val tvShow = state.tvShow
        val reviews = state.reviews
        this.isVisible = true
        configureBackDropUI(tvShow)
        configurePosterUI(tvShow)
        configureRatingUI(tvShow)
        configureCountEpisodesUI(tvShow)
        configureReleaseDateUI(tvShow)
        configureOverviewUI(tvShow)
        configureGenresUI(tvShow)
        configureProductionCompaniesUI(tvShow)
        configureCreatedByUI(tvShow)
        configureSeasonsUI(tvShow)
        configureVideosUI(state.videos)
        configureReviewUI(reviews)
    }

    private fun configureVideosUI(videos: ArrayList<VideoEntity>) {
        if (videos.isNotEmpty()) {
            val videoAdapter = VideosAdapter(viewModel).also { it.updateData(videos) }
            with(binding.videosRecyclerView) {
                this.adapter = videoAdapter
                applyDecoration()
            }
        } else {
            binding.videosTextView.visibility = View.GONE
            binding.videosRecyclerView.visibility = View.GONE
        }
    }

    private fun configureSeasonsUI(tvShow: TvShowDetailsEntity) = with(binding) {
        val seasonsValue = tvShow.seasons
        if(!seasonsValue.isNullOrEmpty()) {
            val seasonAdapter = SeasonAdapter(seasonsValue, seasonSimpleListener)
            with(seasonsRecyclerView) {
                this.adapter = seasonAdapter
                applyDecoration()
            }
        } else {
            seasonsHeader.isVisible = false
            seasonsRecyclerView.isVisible = false
        }
    }

    @SuppressLint("ResourceType")
    private fun showSeasonModalBottomSheet(season: SeasonEntity) {
        val seasonNumber = season.seasonNumber?.toString() ?: return
        val fragment = SeasonDetailsBottomSheetDialogFragment.newInstance(
            season = season,
            seasonNumber = seasonNumber,
            seriesId = args.id
        )
        fragment.show(childFragmentManager, SEASON_DETAILS_MODAL_BOTTOM_SHEET_TAG)
        childFragmentManager.setFragmentResultListener(SeasonDetailsBottomSheetDialogFragment.NAVIGATE_TO_EPISODES_CODE, viewLifecycleOwner) { _, data ->
            val seriesIdArg = data.getString(SeasonDetailsBottomSheetDialogFragment.SERIES_ID_ARG)
            ?: throw IllegalStateException("The seasonId argument must be passed")
            val seasonNumberArg = data.getString(SeasonDetailsBottomSheetDialogFragment.SEASON_NUMBER_ARG)
            ?: throw IllegalStateException("The seasonNumber argument must be passed")
            viewModel.navigateToEpisodes(seriesIdArg, seasonNumberArg)
        }
    }

    private fun configureCreatedByUI(tvShow: TvShowDetailsEntity) = with(binding) {
        val createdBy = tvShow.createdBy
        if(!createdBy.isNullOrEmpty()) {
            val creatorAdapter = CreatorAdapter(createdBy, personSimpleListener)
            with(creators) {
                this.adapter = creatorAdapter
                applyDecoration()
            }
        } else {
            createdByHeader.isVisible = false
            creators.isVisible = false 
        }
    }

    private fun configureOverviewUI(tvShow: TvShowDetailsEntity) = with(binding) {
        val overview = tvShow.overview
        val nullOrEmpty = overview.isNullOrEmpty()
        if(!nullOrEmpty) {
            overviewText.text = overview
        } else {
            overviewHeader.visibility = View.GONE
            overviewText.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun configureRatingUI(tvShow: TvShowDetailsEntity) = with(binding) {
        binding.ratingBar.isEnabled = false
        val rating = tvShow.rating ?: return@with
        if(rating > 0) {
            binding.ratingText.text = "%.2f".format(rating)
            binding.ratingBar.rating = (rating.toFloat() / 2)
        } else {
            ratingText.isVisible = false
            ratingBar.isVisible = false
        }
    }

    private fun configurePosterUI(tvShow: TvShowDetailsEntity) = with(binding.tvShowsPosterImageView) {
        val poster = tvShow.poster
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

    private fun configureBackDropUI(tvShow: TvShowDetailsEntity) = with(binding.backDrop) {
        val backDrop = tvShow.backDrop
        if(!backDrop.isNullOrEmpty()) {
            Glide
                .with(this)
                .load(backDrop)
                .centerCrop()
                .placeholder(R.drawable.backdrop_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        } else {
            Glide
                .with(context)
                .load(R.drawable.backdrop_placeholder_bg)
                .centerCrop()
                .into(this)
        }
    }

    private fun configureCountEpisodesUI(tvShow: TvShowDetailsEntity) = with(binding.countEpisodesValue) {
        val countEpisodes = tvShow.numberOfEpisodes ?: return@with
        text = countEpisodes.toString()
    }

    @SuppressLint("SimpleDateFormat")
    private fun configureReleaseDateUI(tvShow: TvShowDetailsEntity) {
        if (tvShow.firstAirDate != null) {
            val simpleDateFormatter = SimpleDateFormat("d MMMM yyyy")
            val date = simpleDateFormatter.format(tvShow.firstAirDate)
            binding.releaseDateValue.text = date
        } else {
            binding.releaseDateHeader.visibility = View.GONE
            binding.releaseDateValue.visibility = View.GONE
        }
    }

    private fun configureFailureUI(state: TvShowDetailsState.Failure) {
        val header = state.header
        val error = state.error
        val failurePartBinding = FailurePartBinding.bind(binding.failurePart.root)
        binding.failureContainer.isVisible = true
        with(failurePartBinding) {
            retryButton.setOnClickListener { viewModel.fetchData() }
            failureTextHeader.isVisible = header != null
            failureTextMessage.isVisible = error != null
            if(header != null) failureTextHeader.text = resources.getString(header)
            if(error != null) failureTextMessage.text = resources.getString(error)
        }
    }

    private fun configureInPendingUI() {
        binding.progressContainer.isVisible = true
    }

    private fun handleTitleState(value: String?) {
        navigator().targetNavigator {
            val title = it.getToolbar()?.title
            if(title == value) return@targetNavigator
            it.getToolbar()?.title = value
        }
    }

    private fun configureGenresUI(tvShowDetails: TvShowDetailsEntity) {
        val genres = tvShowDetails.genres
        if (!genres.isNullOrEmpty()) {
            val layoutManager = FlexboxLayoutManager(requireContext())
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            binding.genresView.layoutManager = layoutManager
            val names = genres.map { it.name }
            val adapter = InfoAdapter(names)
            binding.genresView.adapter = adapter
        } else {
            binding.genresHeader.visibility = View.GONE
            binding.genresView.visibility = View.GONE
        }
    }

    private fun configureProductionCompaniesUI(tvShowDetails: TvShowDetailsEntity) {
        val  productionCompanies = tvShowDetails.productionCompanies
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

    private fun configureReviewUI(reviews: ArrayList<ReviewEntity>) {
        if(reviews.isNotEmpty()) {
            val review = reviews.first()
            configureReviewAuthorUI(review)
            configureReviewRatingUI( review)
            configureReviewContentUI(review)
            configureReviewAvatarUI(review)
            this@TvShowDetailsFragment.binding.showAllReviewsButton.setOnClickListener {
                viewModel.navigateToTvReviews()
            }
        } else {
            this@TvShowDetailsFragment.binding.reviewsHeaderTextView.isVisible = false
            this@TvShowDetailsFragment.binding.showAllReviewsButton.isVisible = false
            binding.reviewItem.reviewTile.isVisible = false
        }
    }

    private fun configureReviewAvatarUI(review: ReviewEntity) = with(binding.reviewItem.avatarImageView) {
        val avatar = review.authorDetails?.avatar
        if(!avatar.isNullOrEmpty()) {
            Glide
                .with(requireContext())
                .load(avatar)
                .centerCrop()
                .placeholder(R.drawable.poster_placeholder_bg)
                .error(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        } else {
            Glide
                .with(requireContext())
                .load(R.drawable.poster_placeholder_bg)
                .placeholder(R.drawable.poster_placeholder_bg)
                .error(R.drawable.poster_placeholder_bg)
                .centerCrop()
                .into(this)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun configureReviewContentUI(review: ReviewEntity) = with(binding.reviewItem.reviewTextView) {
        val content = review.content
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

    companion object {
        const val SEASON_DETAILS_MODAL_BOTTOM_SHEET_TAG = "ModalBottomSheetTag"
        const val PERSON_DETAILS_MODAL_BOTTOM_SHEET_TAG = "PersonDetailsModalBottomSheetTag"
    }

}