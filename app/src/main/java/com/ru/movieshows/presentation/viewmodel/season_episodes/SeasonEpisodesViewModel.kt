package com.ru.movieshows.presentation.viewmodel.season_episodes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.R
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.SeasonEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.screens.season_episodes.SeasonEpisodesFragmentArgs
import com.ru.movieshows.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.season_episodes.state.SeasonEpisodesState
import com.ru.movieshows.presentation.viewmodel.share
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class SeasonEpisodesViewModel @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val arguments: SeasonEpisodesFragmentArgs,
    private val tvShowRepository: TvShowRepository,
) : BaseViewModel() {

    private val _state = MutableLiveData<SeasonEpisodesState>(SeasonEpisodesState.InPending)
    val state = _state.share()

    init {
        viewModelScope.launch {
            languageTagFlow.collect { language ->
                getEpisodes(language)
            }
        }
    }

    fun tryAgain() {
        viewModelScope.launch {
            getEpisodes(languageTag)
        }
    }

    private suspend fun getEpisodes(language: String) {
        val seriesId = arguments.seriesId
        val seasonNumber = arguments.seasonNumber
        _state.value = SeasonEpisodesState.InPending
        val getSeasonDetailsResult = tvShowRepository.getSeason(language, seriesId, seasonNumber)
        getSeasonDetailsResult.fold(::onSuccessGetEpisodes, ::onFailureGetEpisodes)
    }

    private fun onFailureGetEpisodes(throwable: Throwable) {
        if(throwable is AppFailure) {
            val headerError = throwable.headerResource()
            val messageError = throwable.errorResource()
            val failureState = SeasonEpisodesState.Failure(headerError, messageError)
            _state.value = failureState
        } else {
            _state.value = SeasonEpisodesState.Failure(R.string.error_header, R.string.an_error_occurred_during_the_operation)
        }
    }

    private fun onSuccessGetEpisodes(seasonEntity: SeasonEntity) {
        val episodes = seasonEntity.episodes
        if(!episodes.isNullOrEmpty()) {
            _state.value = SeasonEpisodesState.Success(episodes)
        } else {
            _state.value = SeasonEpisodesState.SuccessEmpty
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator, arguments: SeasonEpisodesFragmentArgs): SeasonEpisodesViewModel
    }

}