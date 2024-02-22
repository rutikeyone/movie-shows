package com.ru.movieshows.app.presentation.viewmodel.episode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.model.tvshows.TvShowRepository
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.app.presentation.screens.episodes.SeasonEpisodesFragmentArgs
import com.ru.movieshows.app.presentation.screens.episodes.SeasonEpisodesFragmentDirections
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.app.presentation.viewmodel.episode.state.SeasonEpisodesState
import com.ru.movieshows.app.utils.share
import com.ru.movieshows.sources.tvshows.entities.EpisodeEntity
import com.ru.movieshows.sources.tvshows.entities.SeasonEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class SeasonEpisodesViewModel @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val arguments: SeasonEpisodesFragmentArgs,
    private val tvShowRepository: TvShowRepository,
) : BaseViewModel(), SimpleAdapterListener<EpisodeEntity> {

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
        getSeasonDetailsResult.fold(::onFailureGetEpisodes, ::onSuccessGetEpisodes)
    }

    private fun onFailureGetEpisodes(e: AppFailure) {
        val headerError = e.headerResource()
        val messageError = e.errorResource()
        val failureState = SeasonEpisodesState.Failure(headerError, messageError)
        _state.value = failureState
    }

    private fun onSuccessGetEpisodes(seasonEntity: SeasonEntity) {
        val episodes = seasonEntity.episodes
        if(!episodes.isNullOrEmpty()) {
            _state.value = SeasonEpisodesState.Success(episodes)
        } else {
            _state.value = SeasonEpisodesState.SuccessEmpty
        }
    }

    override fun onClickItem(data: EpisodeEntity) {
        val seriesId = arguments.seriesId
        val seasonNumber = arguments.seasonNumber
        val episodeNumber = data.episodeNumber ?: return
        val direction = SeasonEpisodesFragmentDirections
        val action = direction.actionSeasonEpisodesFragmentToEpisodeDetailsFragment(
            seriesId = seriesId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
        )
        navigator.navigate(action)
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator, arguments: SeasonEpisodesFragmentArgs): SeasonEpisodesViewModel
    }

}