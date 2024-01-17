package com.ru.movieshows.app.presentation.viewmodel.tv_shows

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.getOrElse
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.model.people.PeopleRepository
import com.ru.movieshows.app.model.tv_shows.TvShowRepository
import com.ru.movieshows.app.presentation.screens.tv_shows.TvShowDetailsFragmentDirections
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.app.presentation.viewmodel.tv_shows.state.TvShowDetailsState
import com.ru.movieshows.app.utils.share
import com.ru.movieshows.sources.movies.entities.ReviewEntity
import com.ru.movieshows.sources.movies.entities.VideoEntity
import com.ru.movieshows.sources.tv_shows.entities.TvShowDetailsEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TvShowDetailsViewModel @AssistedInject constructor(
    @Assisted private val tvShowId: String,
    @Assisted private val navigator: Navigator,
    private val tvShowRepository: TvShowRepository,
    private val peopleRepository: PeopleRepository,
) : BaseViewModel() {
    private val _state = MutableLiveData<TvShowDetailsState>(TvShowDetailsState.Empty)
    val state get() = _state.share()

    private val _titleState = MutableLiveData("")
    val titleState = _titleState.share()

    init {
        fetchData()
    }

    fun fetchData() = viewModelScope.launch {
        languageTagFlow.collect {
            _titleState.value = ""
            _state.value = TvShowDetailsState.InPending
            try {
                val tvShow = getTvShowById(it)
                val videos = getVideosById(it)
                val reviews = getReviewsBySeriesId(it).second
                _titleState.value = tvShow.name
                _state.value = TvShowDetailsState.Success(tvShow, videos, reviews)
            } catch (e: AppFailure) {
                val failureState = TvShowDetailsState.Failure(e.headerResource(), e.errorResource())
                _state.value = failureState
            }
        }
    }

    private suspend fun getTvShowById(language: String): TvShowDetailsEntity {
        val fetchTvShowDetailsResult = tvShowRepository.getTvShowDetails(language, tvShowId)
        return fetchTvShowDetailsResult.getOrElse {
            throw it
        }
    }

    private suspend fun getVideosById(language: String): ArrayList<VideoEntity> {
        val fetchVideosByIdResult = tvShowRepository.getVideosByMovieId(language, tvShowId)
        return fetchVideosByIdResult.getOrElse {
            throw it
        }
    }

    private suspend fun getReviewsBySeriesId(language: String): Pair<Int, ArrayList<ReviewEntity>> {
        val page = 1
        val fetchReviewsByIdResult = tvShowRepository.getTvReviews(language, tvShowId, page)
        return fetchReviewsByIdResult.getOrElse {
            throw it
        }
    }

    fun navigateToVideo(video: VideoEntity) {
        val action = TvShowDetailsFragmentDirections.actionTvShowDetailsFragmentToVideoPlayerActivity(video)
        navigator.navigate(action)
    }

    fun navigateToTvReviews() {
        val action = TvShowDetailsFragmentDirections.actionTvShowDetailsFragmentToTvReviewsFragment(tvShowId)
        navigator.navigate(action)
    }

    fun navigateToEpisodes(seriesId: String, seasonNumber: String) {
        val action = TvShowDetailsFragmentDirections.actionTvShowDetailsFragmentToSeasonEpisodesFragment(
            seriesId = seriesId,
            seasonNumber = seasonNumber,
        )
        navigator.navigate(action)
    }

    @AssistedFactory
    interface Factory {
        fun create(movieId: String, navigator: Navigator): TvShowDetailsViewModel
    }
}