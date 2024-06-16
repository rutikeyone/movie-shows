package com.ru.movieshows.tv_shows.presentation.episode_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.core.presentation.BaseFragment
import com.ru.movieshows.core.presentation.BaseScreen
import com.ru.movieshows.core.presentation.R
import com.ru.movieshows.core.presentation.applyDecoration
import com.ru.movieshows.core.presentation.args
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.core.presentation.viewModelCreator
import com.ru.movieshows.core.presentation.views.observe
import com.ru.movieshows.navigation.GlobalNavComponentRouter
import com.ru.movieshows.tv_shows.TvShowsRouter
import com.ru.movieshows.tv_shows.domain.entities.Episode
import com.ru.movieshows.tv_shows.domain.entities.Season
import com.ru.movieshows.tv_shows.presentation.views.CrewAdapter
import com.ru.movieshows.tvshows.databinding.FragmentEpisodeDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class EpisodeDetailsFragment : BaseFragment() {

    data class Screen(
        val seriesId: String,
        val seasonNumber: String,
        val episodeNumber: Int,
    ) : BaseScreen


    @Inject
    lateinit var globalNavComponentRouter: GlobalNavComponentRouter

    @Inject
    lateinit var factory: EpisodeDetailsViewModel.Factory

    @Inject
    lateinit var router: TvShowsRouter

    private val binding by viewBinding<FragmentEpisodeDetailsBinding>()

    override val viewModel by viewModelCreator {
        factory.create(args())
    }

    private val crewAdapter by lazy {
        CrewAdapter() { crew ->
            val id = crew.id?.toString()

            router.launchPersonDetailsDialog(id, childFragmentManager)

        }
     }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(
        com.ru.movieshows.tvshows.R.layout.fragment_episode_details,
        container,
        false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.root) {
            setTryAgainListener { viewModel.toTryAgain() }

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

    private fun setupViews(state: EpisodeDetailsViewModel.State) {

        val season = state.season
        val episode = state.episode

        setupBackdropImageView(season)
        setupPosterImageView(episode)
        setupEpisodeNameView(episode)
        setupRatingView(episode)
        setupReleaseDateView(episode)
        setupOverviewView(episode)
        setupCrewView(episode)
    }

    private fun setupCrewView(episode: Episode) {
        val crews = episode.crew

        if (!crews.isNullOrEmpty()) {
            crewAdapter.submitData(crews)

            with(binding.episodeCrewRecyclerView) {
                this.adapter = crewAdapter
                isVisible = true
                applyDecoration()
            }

            binding.crewHeaderTextView.isVisible = true

        } else {
            binding.crewHeaderTextView.isVisible = true
            binding.episodeCrewRecyclerView.isVisible = true
        }
    }

    private fun setupOverviewView(episode: Episode) {

        val overview = episode.overview

        with(binding) {
            if (!overview.isNullOrEmpty()) {
                overviewTextView.text = overview
            } else {
                overviewHeaderTextView.isVisible = true
                overviewTextView.isVisible = false
            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun setupReleaseDateView(episode: Episode) {
        val date = episode.airDate
        val simpleDateFormatter = SimpleDateFormat("d MMMM yyyy")

        with(binding) {

            if (date != null) {
                val formattedDate = simpleDateFormatter.format(date)
                releaseDateValueTextView.text = formattedDate
            } else {
                releaseDateHeaderTextView.isVisible = false
                releaseDateValueTextView.isVisible = false
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setupRatingView(episode: Episode) {

        val rating = episode.rating

        with(binding) {
            ratingBar.isEnabled = false

            if (rating != null && rating > 0) {
                val ratingValue = (rating.toFloat() / 2)

                ratingTextView.text = "%.2f".format(rating)
                ratingBar.rating = ratingValue
            } else {
                ratingTextView.isVisible = false
                ratingBar.isVisible = false
            }

        }

    }

    private fun setupEpisodeNameView(episode: Episode) {

        val name = episode.name

        with(binding.episodeNameTextView) {
            if (!name.isNullOrEmpty()) {
                text = name
                isVisible = true
            } else {
                isVisible = false
            }
        }

    }

    private fun setupPosterImageView(episode: Episode) {

        val loadStillPath = episode.stillPath

        with(binding.episodePosterImageView) {
            Glide
                .with(context)
                .load(loadStillPath)
                .placeholder(R.drawable.core_presentation_bg_poster_placeholder)
                .error(R.drawable.core_presentation_bg_poster_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(this)
        }

    }

    private fun setupBackdropImageView(season: Season) {

        val loadPosterPath = season.posterPath

        with(binding.seasonBackDropImageView) {

            Glide
                .with(this)
                .load(loadPosterPath)
                .placeholder(R.drawable.core_presentation_bg_backdrop_placeholder)
                .error(R.drawable.core_presentation_bg_backdrop_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(this)
        }
    }

}