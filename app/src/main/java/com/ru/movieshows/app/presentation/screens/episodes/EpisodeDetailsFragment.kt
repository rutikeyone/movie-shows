package com.ru.movieshows.app.presentation.screens.episodes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.adapters.episode.CrewAdapter
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.screens.tv_shows.PersonDetailsBottomSheetDialogFragment
import com.ru.movieshows.app.presentation.viewmodel.episode.EpisodeDetailsViewModel
import com.ru.movieshows.app.presentation.viewmodel.episode.state.EpisodeDetailsStatus
import com.ru.movieshows.app.utils.applyDecoration
import com.ru.movieshows.app.utils.observeEvent
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.FragmentEpisodeDetailsBinding
import com.ru.movieshows.sources.people.entities.PersonEntity
import com.ru.movieshows.sources.tvshows.entities.CrewEntity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class EpisodeDetailsFragment : BaseFragment() {

    private val binding by viewBinding<FragmentEpisodeDetailsBinding>()

    private val arguments by navArgs<EpisodeDetailsFragmentArgs>()

    @Inject
    lateinit var factory: EpisodeDetailsViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(
            arguments = arguments,
            toasts = toasts(),
            resources = resources(),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_episode_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { state ->
            configureTitleUI(state.title)
            handleEpisodeStatus(state.status)
        }
        viewModel.showPeopleDetailsEvent.observeEvent(viewLifecycleOwner, ::showPeopleDetailsModalBottomSheet)
    }

    private fun showPeopleDetailsModalBottomSheet(person: PersonEntity) {
        val fragment = PersonDetailsBottomSheetDialogFragment.newInstance(person)
        fragment.show(childFragmentManager, PERSON_DETAILS_MODAL_BOTTOM_SHEET_TAG)
    }

    private fun handleEpisodeStatus(status: EpisodeDetailsStatus) {
        binding.root.forEach { it.isVisible = false }
        when (status) {
            EpisodeDetailsStatus.Empty -> {}
            EpisodeDetailsStatus.InPending -> configureInPendingUI()
            is EpisodeDetailsStatus.Success -> configureSuccessUI(status)
            is EpisodeDetailsStatus.Failure -> configureFailureUI(status)
        }
    }

    private fun configureFailureUI(status: EpisodeDetailsStatus.Failure) {
        val header = status.header
        val error = status.error
        FailurePartBinding.bind(binding.failurePart.root).also {
            it.retryButton.setOnClickListener { viewModel.tryAgain() }
            if(header != null) it.failureTextHeader.text = resources.getString(header)
            if(error != null) it.failureTextMessage.text = resources.getString(error)
        }
        binding.failureContainer.isVisible = true
    }

    private fun configureSuccessUI(state: EpisodeDetailsStatus.Success) {
        val season = state.season
        val episode = state.episode
        binding.successGroup.isVisible = true
        configureSeasonBackDropUI(season.poster)
        configureSeasonPosterUI(episode.stillPath)
        configureEpisodeNameUI(episode.name)
        configureRatingUI(episode.rating)
        configureReleaseDateUI(episode.airDate)
        configureOverviewUI(episode.overview)
        configureCrewUI(episode.crew)
    }

    private fun configureCrewUI(crew: ArrayList<CrewEntity>?) = with(binding) {
        if (!crew.isNullOrEmpty()) {
            val adapter = CrewAdapter(crew, viewModel)
            episodeCrewRecyclerView.applyDecoration()
            episodeCrewRecyclerView.adapter = adapter
            episodeCrewRecyclerView.isVisible = true
            crewHeaderTextView.isVisible = true
        } else {
            episodeCrewRecyclerView.isVisible = false
            crewHeaderTextView.isVisible = false
        }
    }

    private fun configureRatingUI(rating: Double?) = with(binding) {
        ratingBar.isEnabled = false
        if (rating != null && rating > 0) {
            ratingText.text = "%.2f".format(rating)
            ratingBar.rating = (rating.toFloat() / 2)
        } else {
            ratingText.isVisible = false
            ratingBar.isVisible = false
        }
    }

    private fun configureEpisodeNameUI(name: String?) = with(binding.episodeNameTextView) {
        if (!name.isNullOrEmpty()) {
            text = name
            isVisible = true
        } else {
            isVisible = false
        }
    }

    private fun configureSeasonPosterUI(image: String?) = with(binding.episodePosterImageView) {
        if (!image.isNullOrEmpty()) {
            Glide
                .with(context)
                .load(image)
                .placeholder(R.drawable.poster_placeholder_bg)
                .error(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(this)
        } else {
            Glide
                .with(context)
                .load(R.drawable.poster_placeholder_bg)
                .placeholder(R.drawable.poster_placeholder_bg)
                .error(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(this)
        }
    }

    private fun configureSeasonBackDropUI(image: String?) = with(binding.seasonBackDropImageView) {
        if (!image.isNullOrEmpty()) {
            Glide
                .with(context)
                .load(image)
                .placeholder(R.drawable.backdrop_placeholder_bg)
                .error(R.drawable.backdrop_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(this)
        } else {
            Glide
                .with(context)
                .load(R.drawable.backdrop_placeholder_bg)
                .placeholder(R.drawable.backdrop_placeholder_bg)
                .error(R.drawable.backdrop_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(this)
        }
    }

    private fun configureInPendingUI() {
        binding.progressGroup.isVisible = true
    }

    private fun configureTitleUI(title: String) {
        navigator().targetNavigator {
            it.getToolbar()?.title = title
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun configureReleaseDateUI(date: Date?) {
        val simpleDateFormatter = SimpleDateFormat("d MMMM yyyy")
        if (date != null) {
            val formattedDate = simpleDateFormatter.format(date)
            binding.releaseDateValue.text = formattedDate
        } else {
            binding.releaseDateHeader.isVisible = false
            binding.releaseDateValue.isVisible = false
        }
    }

    private fun configureOverviewUI(overview: String?) {
        if (!overview.isNullOrEmpty()) {
            binding.overviewText.text = overview
        } else {
            binding.overviewHeader.isVisible = false
            binding.overviewText.isVisible = false
        }
    }

    companion object {
        const val PERSON_DETAILS_MODAL_BOTTOM_SHEET_TAG = "PersonDetailsModalBottomSheetTag"
    }

}