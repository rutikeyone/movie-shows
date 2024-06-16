package com.ru.movieshows.tv_shows.presentation.episode_details

import com.ru.movieshows.core.Container
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.tv_shows.domain.GetEpisodeByNumberUseCase
import com.ru.movieshows.tv_shows.domain.GetSeasonUseCase
import com.ru.movieshows.tv_shows.domain.entities.Episode
import com.ru.movieshows.tv_shows.domain.entities.Season
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EpisodeDetailsViewModel @AssistedInject constructor(
    @Assisted private val args: EpisodeDetailsFragment.Screen,
    private val getSeasonUseCase: GetSeasonUseCase,
    private val getEpisodeByNumberUseCase: GetEpisodeByNumberUseCase,
) : BaseViewModel() {

    private val loadScreenStateFlow = MutableStateFlow<Container<State>>(Container.Pending)

    private val titleStateFlow = MutableStateFlow("")

    val loadScreenStateLiveValue = loadScreenStateFlow
        .toLiveValue(Container.Pending)

    val titleStateLiveValue = titleStateFlow
        .toLiveValue("")


    init {
        viewModelScope.launch {
            languageTagFlow.collect { language ->
                getData(language)
            }
        }
    }

    fun toTryAgain() {
        viewModelScope.launch {
            getData(languageTag)
        }
    }

    private suspend fun getData(language: String) {

        val seasonNumber = args.seasonNumber
        val seriesId = args.seriesId
        val episodeNumber = args.episodeNumber

        titleStateFlow.value = ""
        loadScreenStateFlow.value = Container.Pending

        try {
            val season = getSeasonUseCase.execute(
                language = language,
                seasonNumber = seasonNumber,
                seriesId = seriesId,
            )

            val episode = getEpisodeByNumberUseCase.execute(
                language = language,
                seasonNumber = seasonNumber,
                seriesId = seriesId,
                episodeNumber = episodeNumber,
            )

            val state = State(season, episode)

            titleStateFlow.value = episode.name ?: ""
            loadScreenStateFlow.value = Container.Success(state)

        } catch (e: Exception) {
            loadScreenStateFlow.value = Container.Error(e)
        }
    }

    data class State(
        val season: Season,
        val episode: Episode,
    )

    @AssistedFactory
    interface Factory {
        fun create(args: EpisodeDetailsFragment.Screen): EpisodeDetailsViewModel
    }

}