package com.ru.movieshows.app.presentation.viewmodel.episode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.getOrElse
import com.ru.movieshows.R
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.model.people.PeopleRepository
import com.ru.movieshows.app.model.tv_shows.TvShowRepository
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.app.presentation.screens.episodes.EpisodeDetailsFragmentArgs
import com.ru.movieshows.app.presentation.sideeffects.resources.Resources
import com.ru.movieshows.app.presentation.sideeffects.toast.Toasts
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.app.presentation.viewmodel.episode.state.EpisodeDetailsState
import com.ru.movieshows.app.presentation.viewmodel.episode.state.EpisodeDetailsStatus
import com.ru.movieshows.app.utils.MutableLiveEvent
import com.ru.movieshows.app.utils.publishEvent
import com.ru.movieshows.app.utils.share
import com.ru.movieshows.sources.people.entities.PersonEntity
import com.ru.movieshows.sources.tv_shows.entities.CrewEntity
import com.ru.movieshows.sources.tv_shows.entities.EpisodeEntity
import com.ru.movieshows.sources.tv_shows.entities.SeasonEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class EpisodeDetailsViewModel @AssistedInject constructor(
    @Assisted private val arguments: EpisodeDetailsFragmentArgs,
    @Assisted private val toasts: Toasts,
    @Assisted private val resources: Resources,
    private val tvShowRepository: TvShowRepository,
    private val peopleRepository: PeopleRepository,
) : BaseViewModel(), SimpleAdapterListener<CrewEntity> {

    private val _state = MutableLiveData(
        EpisodeDetailsState(
            seriesId = arguments.seriesId,
            seasonNumber = arguments.seasonNumber,
            episodeNumber = arguments.episodeNumber,
            title = "",
        )
    )
    val state = _state.share()

    private val currentState get() = _state.value!!

    private val _showPeopleDetailsEvent = MutableLiveEvent<PersonEntity>()
    val showPeopleDetailsEvent = _showPeopleDetailsEvent.share()

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
        val getSeasonResult = tvShowRepository.getSeason(language, seriesId, seasonNumber)
        return getSeasonResult.getOrElse {
            throw it
        }
    }

    private suspend fun getEpisodeDetails(language: String): EpisodeEntity {
        val seriesId = currentState.seriesId
        val seasonNumber = currentState.seasonNumber
        val episodeNumber = currentState.episodeNumber
        val getEpisodeResult = tvShowRepository.getEpisodeByNumber(language, seriesId, seasonNumber, episodeNumber)
        return getEpisodeResult.getOrElse {
            throw it
        }
    }

    private fun handleGetPersonDetailsSuccessResult(personEntity: PersonEntity) {
        _showPeopleDetailsEvent.publishEvent(personEntity)
    }

    private fun handleGetPersonDetailsFailureResult(appFailure: AppFailure) {
        val errorResource = appFailure.errorResource()
        val error = resources.getString(errorResource)
        toasts.toast(error)
    }

    override fun onClickItem(data: CrewEntity) {
        viewModelScope.launch {
            val personId = data.id.toString()
            val getPersonDetailResult = peopleRepository.getPersonDetails(
                personId,
                languageTag
            )
            getPersonDetailResult.fold(
                ::handleGetPersonDetailsFailureResult,
                ::handleGetPersonDetailsSuccessResult
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            arguments: EpisodeDetailsFragmentArgs,
            toasts: Toasts,
            resources: Resources,
        ): EpisodeDetailsViewModel
    }

}