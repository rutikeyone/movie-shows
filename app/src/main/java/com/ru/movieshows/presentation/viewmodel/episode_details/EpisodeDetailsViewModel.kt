package com.ru.movieshows.presentation.viewmodel.episode_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
        getEpisodeDetails()
    }

    private fun getEpisodeDetails() = viewModelScope.launch {
        languageTagFlow.collect { language ->
            _state.value = currentState.copy(status = EpisodeDetailsStatus.InPending)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(arguments: EpisodeDetailsFragmentArgs): EpisodeDetailsViewModel
    }

}