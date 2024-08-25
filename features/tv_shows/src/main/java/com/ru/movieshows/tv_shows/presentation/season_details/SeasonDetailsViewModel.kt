package com.ru.movieshows.tv_shows.presentation.season_details

import com.ru.movieshows.core.Container
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.TvShowsRouter
import com.ru.movieshows.tv_shows.domain.GetImagesBySeasonNumberUseCase
import com.ru.movieshows.tv_shows.domain.GetSeasonUseCase
import com.ru.movieshows.tv_shows.domain.GetVideosBySeasonNumberUseCase
import com.ru.movieshows.tv_shows.domain.entities.Season
import com.ru.movieshows.tv_shows.domain.entities.Video
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SeasonDetailsViewModel @AssistedInject constructor(
    @Assisted private val args: SeasonDetailsBottomSheetDialogFragment.Screen,
    private val getSeasonUseCase: GetSeasonUseCase,
    private val getVideosBySeasonNumberUseCase: GetVideosBySeasonNumberUseCase,
    private val getImagesBySeasonNumberUseCase: GetImagesBySeasonNumberUseCase,
    private val router: TvShowsRouter,
) : BaseViewModel() {

    private val loadScreenStateFlow = MutableStateFlow<Container<State>>(Container.Pending)

    val loadScreenStateLiveValue = loadScreenStateFlow
        .toLiveValue(Container.Pending)

    val videoSimpleAdapterListener = SimpleAdapterListener<Video> {
        launchVideo(it)
    }

    init {
        viewModelScope.launch {
            languageTagFlow.collect { language ->
                getData(language)
            }
        }
    }

    fun toTryAgain() {
        debounce {
            viewModelScope.launch {
                getData(languageTag)
            }
        }
    }

    private suspend fun getData(
        language: String,
        silentMode: Boolean = false,
    ) {
        if (!silentMode) {
            loadScreenStateFlow.value = Container.Pending
        }

        try {
            val season = getSeasonUseCase.execute(
                language = language,
                seriesId = args.seriesId,
                seasonNumber = args.seasonNumber,
            )

            val videos = getVideosBySeasonNumberUseCase.execute(
                language = language,
                seriesId = args.seriesId,
                seasonNumber = args.seasonNumber,
            )

            val images = getImagesBySeasonNumberUseCase.execute(
                language = language,
                seriesId = args.seriesId,
                seasonNumber = args.seasonNumber,
            )

            val state = State(
                season = season,
                videos = videos,
                images = images,
            )

            loadScreenStateFlow.value = Container.Success(state)

        } catch (e: Exception) {
            loadScreenStateFlow.value = Container.Error(e)
        }
    }

    fun updateSeason() = debounce {
        val loadState = loadScreenStateFlow.value

        if (loadState is Container.Success) {
            viewModelScope.launch {
                getData(
                    language = languageTag,
                    silentMode = true,
                )
            }
        }

    }

    private fun launchVideo(video: Video) {
        val key = video.key

        key?.let {
            router.launchVideo(it)
        }
    }

    data class State(
        val season: Season,
        val videos: List<Video>,
        val images: List<String>?,
    )

    @AssistedFactory
    interface Factory {
        fun create(
            args: SeasonDetailsBottomSheetDialogFragment.Screen,
        ): SeasonDetailsViewModel
    }

}