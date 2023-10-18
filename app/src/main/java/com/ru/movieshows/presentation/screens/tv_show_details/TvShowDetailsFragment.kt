package com.ru.movieshows.presentation.screens.tv_show_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentTvShowDetailsBinding
import com.ru.movieshows.databinding.SeasonModalSheetBinding
import com.ru.movieshows.domain.entity.SeasonEntity
import com.ru.movieshows.domain.entity.TvShowDetailsEntity
import com.ru.movieshows.presentation.adapters.CreatorAdapter
import com.ru.movieshows.presentation.adapters.GenresAdapter
import com.ru.movieshows.presentation.adapters.SeasonAdapter
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.screens.movie_reviews.ItemDecoration
import com.ru.movieshows.presentation.viewmodel.tv_show_details.TvShowDetailsState
import com.ru.movieshows.presentation.viewmodel.tv_show_details.TvShowDetailsViewModel
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
    override val viewModel by viewModelCreator { factory.create(args.id) }

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
        viewModel.title.observe(viewLifecycleOwner, ::renderTitle)
        viewModel.state.observe(viewLifecycleOwner, ::renderUI)
    }

    private fun renderUI(state: TvShowDetailsState) = with(binding) {
        val viewParts = listOf(successContainer, failureContainer, progressContainer)
        viewParts.forEach { it.visibility = View.GONE }
        when(state) {
            TvShowDetailsState.Pure -> {}
            TvShowDetailsState.InPending -> renderInPendingUI()
            is TvShowDetailsState.Failure -> renderFailureUI(state)
            is TvShowDetailsState.Success -> renderSuccessUI(state)
        }
    }

    private fun renderSuccessUI(state: TvShowDetailsState.Success) = with(binding.successContainer) {
        this.visibility = View.VISIBLE
        val tvShow = state.tvShow
        setupBackDrop(tvShow)
        setupPoster(tvShow)
        setupRating(tvShow)
        setupCountEpisodes(tvShow)
        setupReleaseDate(tvShow)
        setupOverview(tvShow)
        setupGenres(tvShow)
        setupCreatedBy(tvShow)
        setupSeasons(tvShow)
    }

    private fun setupSeasons(
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
        val supportFragmentManager = requireActivity().supportFragmentManager
        val bottomSheet = SeasonDetailsBottomSheetDialogFragment()
        val bundle = Bundle().also {
            it.putParcelable(SeasonDetailsBottomSheetDialogFragment.SEASON_ARG, season)
        }
        bottomSheet.also {
            it.arguments = bundle
        }
        bottomSheet.show(supportFragmentManager, seasonModalBottomSheet)
    }

    private fun setupCreatedBy(tvShow: TvShowDetailsEntity) = with(binding) {
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

    private fun setupOverview(tvShow: TvShowDetailsEntity) = with(binding){
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
    private fun setupRating(tvShow: TvShowDetailsEntity) = with(binding.ratingBar) {
        isEnabled = false
        val rating = tvShow.rating ?: return@with
        binding.ratingText.text = "%.2f".format(rating)
        binding.ratingBar.rating = (rating.toFloat() / 2)
    }

    private fun setupPoster(tvShow: TvShowDetailsEntity) = with((binding.poster)) {
        val poster = tvShow.poster ?: return@with
        Glide
            .with(this)
            .load(poster)
            .centerCrop()
            .into(this)
    }

    private fun setupBackDrop(tvShow: TvShowDetailsEntity) = with(binding.backDrop) {
        val backDrop = tvShow.backDrop ?: return@with
        Glide
            .with(this)
            .load(backDrop)
            .centerCrop()
            .into(this)
    }

    private fun setupCountEpisodes(tvShow: TvShowDetailsEntity) = with(binding.countEpisodesValue) {
        val countEpisodes = tvShow.numberOfEpisodes ?: return@with
        text = countEpisodes.toString()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupReleaseDate(tvShow: TvShowDetailsEntity) {
        if (tvShow.firstAirDate != null) {
            val simpleDateFormatter = SimpleDateFormat("d MMMM yyyy")
            val date = simpleDateFormatter.format(tvShow.firstAirDate)
            binding.releaseDateValue.text = date
        } else {
            binding.releaseDateHeader.visibility = View.GONE
            binding.releaseDateValue.visibility = View.GONE
        }
    }

    private fun renderFailureUI(state: TvShowDetailsState.Failure) {
        val header = state.header
        val error = state.error
        val failurePartBinding = FailurePartBinding.bind(binding.failurePart.root)
        binding.failureContainer.visibility = View.VISIBLE
        failurePartBinding.retryButton.setOnClickListener { viewModel.fetchData() }
        if(header != null) failurePartBinding.failureTextHeader.text = resources.getString(header)
        if(error != null) failurePartBinding.failureTextMessage.text = resources.getString(error)
    }

    private fun renderInPendingUI() = with(binding.progressContainer) {
        visibility = View.VISIBLE
    }

    private fun renderTitle(value: String?) {
        val toolBar = activity?.findViewById<MaterialToolbar>(R.id.tabsToolbar) ?: return
        toolBar.title = value
    }

    private fun setupGenres(tvShowDetails: TvShowDetailsEntity) {
        val genres = tvShowDetails.genres
        if (!genres.isNullOrEmpty()) {
            val layoutManager = FlexboxLayoutManager(requireContext())
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            binding.genresView.layoutManager = layoutManager
            val adapter = GenresAdapter(genres)
            binding.genresView.adapter = adapter
        } else {
            binding.genresHeader.visibility = View.GONE
            binding.genresView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val seasonModalBottomSheet = "modal_bottom_sheet";
    }
}