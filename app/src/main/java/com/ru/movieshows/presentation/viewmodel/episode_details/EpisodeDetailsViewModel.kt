package com.ru.movieshows.presentation.viewmodel.episode_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.R
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.EpisodeEntity
import com.ru.movieshows.domain.entity.SeasonEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.screens.episode_details.EpisodeDetailsFragmentArgs
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.episode_details.state.EpisodeDetailsState
import com.ru.movieshows.presentation.viewmodel.episode_details.state.EpisodeDetailsStatus
import com.ru.movieshows.presentation.viewmodel.share
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class EpisodeDetailsViewModel @AssistedInject constructor(
    @Assisted private val arguments: EpisodeDetailsFragmentArgs,
    private val repository: TvShowRepository,
) : BaseViewModel() {

    private val _state = MutableLiveData(
        EpisodeDetailsState(
            seriesId = arguments.seriesId,
            seasonNumber = arguments.seasonNumber,
            episodeNumber = arguments.episodeNumber,
            title = "",
        )
    )

    private val currentState get() = _state.value!!

    val state = _state.share()

    init {
        getData()
    }

    private fun getData() = viewModelScope.launch {
        languageTagFlow.collect { language ->
            _state.value = currentState.copy(status = EpisodeDetailsStatus.InPending)
            try {
                val episodeDetails = getEpisodeDetails(language)
                val season = getSeason(language)
                val successStatus = EpisodeDetailsStatus.Success(season, episodeDetails)
                _state.value  = currentState.copy(status = successStatus, title = episodeDetails.name ?: "")
            } catch (e: AppFailure) {
                val headerError = e.headerResource()
                val messageError = e.errorResource()
                val failureStatus = EpisodeDetailsStatus.Failure(headerError, messageError)
                _state.value = currentState.copy(status = failureStatus)
            } catch (e: Exception) {
                val failureStatus = EpisodeDetailsStatus.Failure(R.string.error_header, R.string.an_error_occurred_during_the_operation)
                _state.value = currentState.copy(status = failureStatus)
            }
        }
    }

    fun tryAgain() = getData()

    private suspend fun getSeason(language: String): SeasonEntity {
        val seriesId = currentState.seriesId
        val seasonNumber = currentState.seasonNumber
        val episodeNumber =  currentState.episodeNumber
        val getSeasonResult = repository.getSeason(language, seriesId, seasonNumber)
        return getSeasonResult.getOrThrow()
    }

    private suspend fun getEpisodeDetails(language: String): EpisodeEntity {
        val seriesId = currentState.seriesId
        val seasonNumber = currentState.seasonNumber
        val episodeNumber = currentState.episodeNumber
        val getEpisodeResult = repository.getEpisodeByNumber(language, seriesId, seasonNumber, episodeNumber)
        return getEpisodeResult.getOrThrow()
    }

    @AssistedFactory
    interface Factory {
        fun create(arguments: EpisodeDetailsFragmentArgs): EpisodeDetailsViewModel
    }

}