package com.ru.movieshows.presentation.screens.tv_show_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentTvShowDetailsBinding
import com.ru.movieshows.domain.entity.SeasonEntity
import com.ru.movieshows.domain.entity.TvShowDetailsEntity
import com.ru.movieshows.domain.entity.VideoEntity
import com.ru.movieshows.presentation.adapters.CreatorAdapter
import com.ru.movieshows.presentation.adapters.InfoAdapter
import com.ru.movieshows.presentation.adapters.ItemDecoration
import com.ru.movieshows.presentation.adapters.SeasonAdapter
import com.ru.movieshows.presentation.adapters.VideosAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.viewmodel.tv_show_details.TvShowDetailsViewModel
import com.ru.movieshows.presentation.viewmodel.tv_show_details.state.TvShowDetailsState
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class TvShowDetailsFragment : BaseFragment() {
    private val args by navArgs<TvShowDetailsFragmentArgs>()

    private var _binding: FragmentTvShowDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: TvShowDetailsViewModel.Factory
    override val viewModel by viewModelCreator {
        factory.create(
            movieId = args.id,
            navigator = navigator(),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.title.observe(viewLifecycleOwner, ::handleTitleState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
    }

    private fun handleState(state: TvShowDetailsState) = with(binding) {
        val viewParts = listOf(successContainer, failureContainer, progressContainer)
        viewParts.forEach { it.isVisible = false }
        when(state) {
            TvShowDetailsState.Pure -> {}
            TvShowDetailsState.InPending -> configureInPendingUI()
            is TvShowDetailsState.Failure -> configureFailureUI(state)
            is TvShowDetailsState.Success -> configureSuccessUI(state)
        }
    }

    private fun configureSuccessUI(state: TvShowDetailsState.Success) = with(binding.successContainer) {
        val tvShow = state.tvShow
        this.isVisible = true
        configureBackDrop(tvShow)
        configurePoster(tvShow)
        configureRating(tvShow)
        configureCountEpisodes(tvShow)
        configureReleaseDate(tvShow)
        configureOverview(tvShow)
        configureGenres(tvShow)
        configureProductionCompanies(tvShow)
        configureCreatedBy(tvShow)
        configureSeasons(tvShow)
        configureVideos(state.videos)
    }

    private fun configureVideos(videos: ArrayList<VideoEntity>) {
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

    private fun configureSeasons(
        tvShow: TvShowDetailsEntity
    ) = with(binding) {
        val seasonsValue = tvShow.seasons
        if(!seasonsValue.isNullOrEmpty()) {
            val itemDecoration = ItemDecoration(8F, resources.displayMetrics)
            val adapter = SeasonAdapter(seasonsValue, ::showSeasonModalBottomSheet)
            seasons.adapter = adapter
            seasons.addItemDecoration(itemDecoration)
        } else {
            seasonsHeader.isVisible = false
            seasons.isVisible = false
        }
    }

    private fun showSeasonModalBottomSheet(season: SeasonEntity) {
        val seasonNumber = season.seasonNumber?.toString() ?: return
        val action = TvShowDetailsFragmentDirections.actionTvShowDetailsFragmentToSeasonDetailsBottomSheetDialogFragment(
            seasonArgs = season,
            seasonNumber = seasonNumber,
            seriesId = args.id
        )
        navigator().navigate(action)
    }

    private fun configureCreatedBy(tvShow: TvShowDetailsEntity) = with(binding) {
        val createdBy = tvShow.createdBy
        if(!createdBy.isNullOrEmpty()) {
            val itemDecoration = ItemDecoration(8F, resources.displayMetrics)
            val adapter = CreatorAdapter(createdBy)
            creators.adapter = adapter
            creators.addItemDecoration(itemDecoration)
        } else {
            createdByHeader.isVisible = false
            creators.isVisible = false 
        }
    }

    private fun configureOverview(tvShow: TvShowDetailsEntity) = with(binding){
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
    private fun configureRating(tvShow: TvShowDetailsEntity) = with(binding) {
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

    private fun configurePoster(tvShow: TvShowDetailsEntity) = with(binding.poster) {
        val poster = tvShow.poster
        if(!poster.isNullOrEmpty()) {
            Glide
                .with(this)
                .load(poster)
                .centerCrop()
                .placeholder(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        } else {
            Glide
                .with(context)
                .load(R.drawable.poster_placeholder_bg)
                .centerCrop()
                .into(this)
        }
    }

    private fun configureBackDrop(tvShow: TvShowDetailsEntity) = with(binding.backDrop) {
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

    private fun configureCountEpisodes(tvShow: TvShowDetailsEntity) = with(binding.countEpisodesValue) {
        val countEpisodes = tvShow.numberOfEpisodes ?: return@with
        text = countEpisodes.toString()
    }

    @SuppressLint("SimpleDateFormat")
    private fun configureReleaseDate(tvShow: TvShowDetailsEntity) {
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

    private fun configureInPendingUI() = with(binding.progressContainer) {
        isVisible = true
    }

    private fun handleTitleState(value: String?) {
        navigator().targetNavigator {
            it.getToolbar()?.title = value
        }
    }

    private fun configureGenres(tvShowDetails: TvShowDetailsEntity) {
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

    private fun configureProductionCompanies(tvShowDetails: TvShowDetailsEntity) {
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun navigateToVideo(video: VideoEntity) = viewModel.navigateToVideo(video)

    companion object {
        const val seasonModalBottomSheet = "modal_bottom_sheet";
    }
}