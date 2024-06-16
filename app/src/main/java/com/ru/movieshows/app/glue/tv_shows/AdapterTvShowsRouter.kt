package com.ru.movieshows.app.glue.tv_shows

import androidx.fragment.app.FragmentManager
import com.ru.movieshows.app.R
import com.ru.movieshows.navigation.GlobalNavComponentRouter
import com.ru.movieshows.tv_shows.TvShowsRouter
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tv_shows.domain.entities.Video
import com.ru.movieshows.tv_shows.presentation.episodes.SeasonEpisodesFragment
import com.ru.movieshows.tv_shows.presentation.reviews.TvShowReviewsFragment
import com.ru.movieshows.tv_shows.presentation.season_details.SeasonDetailsBottomSheetDialogFragment
import com.ru.movieshows.tv_shows.presentation.tv_show_details.TvShowDetailsFragment
import javax.inject.Inject

class AdapterTvShowsRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter,
) : TvShowsRouter {

    override fun launchTvShowSearch() {
        globalNavComponentRouter.launch(R.id.tvShowSearchFragment)
    }

    override fun launchTvShowsDetails(tvShow: TvShow) {
        val id = tvShow.id ?: return
        val args = TvShowDetailsFragment.Screen(id)
        globalNavComponentRouter.launch(R.id.tvShowDetailsFragment, args)
    }

    override fun launchOnTheAirTvShows() {
        globalNavComponentRouter.launch(R.id.onTheAirTvShowsFragment)
    }

    override fun launchTopRatedTvShows() {
        globalNavComponentRouter.launch(R.id.topRatedTvShowsFragment)
    }

    override fun launchPopularTvShows() {
        globalNavComponentRouter.launch(R.id.popularTvShowsFragment)
    }

    override fun launchToEpisodes(seriesId: String, seasonNumber: String) {}

    override fun launchToTvShowReviews(tvShowId: String?) {
        val id = tvShowId ?: return
        val args = TvShowReviewsFragment.Screen(id)

        globalNavComponentRouter.launch(R.id.tvShowReviewsFragment, args)
    }

    override fun launchVideo(video: Video) {}

    override fun launchSeasonDetailsBottomSheetDialog(
        childFragmentManager: FragmentManager,
        seasonNumber: String,
        seriesId: String,
    ) {

        val fragment = SeasonDetailsBottomSheetDialogFragment.newInstance(
            seasonNumber = seasonNumber,
            seriesId = seriesId,
        )

        fragment.show(
            childFragmentManager,
            SEASON_DETAILS_MODAL_BOTTOM_SHEET_TAG
        )

    }

    override fun launchEpisodes(seriesId: String, seasonNumber: String) {
        val args = SeasonEpisodesFragment.Screen(
            seriesId = seriesId,
            seasonNumber = seasonNumber,
        )

        globalNavComponentRouter.launch(R.id.seasonEpisodesFragment, args)
    }

    override fun launchEpisodeDetails() {
        globalNavComponentRouter.launch(R.id.episodeDetailsFragment)
    }

    companion object {
        const val SEASON_DETAILS_MODAL_BOTTOM_SHEET_TAG = "ModalBottomSheetTag"
    }

}