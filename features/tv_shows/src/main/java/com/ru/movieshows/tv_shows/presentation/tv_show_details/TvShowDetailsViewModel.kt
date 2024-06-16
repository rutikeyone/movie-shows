package com.ru.movieshows.tv_shows.presentation.tv_show_details

import com.ru.movieshows.core.Container
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.TvShowsRouter
import com.ru.movieshows.tv_shows.domain.GetTvShowDetailsUseCase
import com.ru.movieshows.tv_shows.domain.GetTvShowReviewsUseCase
import com.ru.movieshows.tv_shows.domain.GetVideosByIdUseCase
import com.ru.movieshows.tv_shows.domain.entities.Review
import com.ru.movieshows.tv_shows.domain.entities.TvShowDetails
import com.ru.movieshows.tv_shows.domain.entities.Video
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TvShowDetailsViewModel @AssistedInject constructor(
    @Assisted private val args: TvShowDetailsFragment.Screen,
    private val router: TvShowsRouter,
    private val getTvShowReviewsUseCase: GetTvShowReviewsUseCase,
    private val getVideosByIdUseCase: GetVideosByIdUseCase,
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase,
) : BaseViewModel() {

    private val loadScreenStateFlow = MutableStateFlow<Container<State>>(Container.Pending)

    private val titleStateFlow = MutableStateFlow("")

    val loadScreenStateLiveValue = loadScreenStateFlow
        .toLiveValue(Container.Pending)

    val titleStateLiveValue = titleStateFlow
        .toLiveValue("")

    val videoSimpleAdapterListener = SimpleAdapterListener<Video> {
        launchVideo(it)
    }

    init {
        viewModelScope.launch {
            languageTagFlow.collect {
                getData(it)
            }
        }
    }

    private suspend fun getData(language: String) {

        titleStateFlow.value = ""
        loadScreenStateFlow.value = Container.Pending

        try {
            val tvShowDetails = getTvShowDetailsUseCase.execute(language, args.id)
            val videos = getVideosByIdUseCase.execute(language, args.id)
            val reviewPagination = getTvShowReviewsUseCase.execute(language, args.id)
            val reviews = reviewPagination.results

            val state = State(
                tvShowDetails = tvShowDetails,
                videos = videos,
                reviews = reviews,
            )

            titleStateFlow.value = tvShowDetails.name ?: ""
            loadScreenStateFlow.value = Container.Success(state)


        } catch (e: Exception) {
            loadScreenStateFlow.value = Container.Error(e)
        }

    }

    fun toTryGetMovieDetails() = debounce {
        viewModelScope.launch {
            getData(languageTag)
        }
    }

    private fun launchVideo(video: Video) = debounce {
        router.launchVideo(video)
    }

    fun launchToTvShowReviews() = debounce {
        router.launchToTvShowReviews(args.id)
    }

    fun launchToEpisodes(seriesId: String, seasonNumber: String) = debounce {
        router.launchToEpisodes(
            seriesId = seriesId,
            seasonNumber = seasonNumber,
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(args: TvShowDetailsFragment.Screen): TvShowDetailsViewModel
    }

    data class State(
        val tvShowDetails: TvShowDetails,
        val videos: List<Video>,
        val reviews: List<Review>,
    )

}