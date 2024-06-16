package com.ru.movieshows.tv_shows.presentation.season_details

import com.ru.movieshows.core.Container
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.tv_shows.domain.GetSeasonUseCase
import com.ru.movieshows.tv_shows.domain.entities.Season
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SeasonDetailsViewModel @AssistedInject constructor(
    @Assisted private val args: SeasonDetailsBottomSheetDialogFragment.Screen,
    private val getSeasonUseCase: GetSeasonUseCase,
) : BaseViewModel() {

    private val loadScreenStateFlow = MutableStateFlow<Container<Season>>(Container.Pending)

    val loadScreenStateLiveValue = loadScreenStateFlow
        .toLiveValue(Container.Pending)

    init {
        viewModelScope.launch {
            languageTagFlow.collect { language ->
                getSeasonData(language)
            }
        }
    }

    fun toTryAgain() {
        debounce {
            viewModelScope.launch {
                getSeasonData(languageTag)
            }
        }
    }

    private suspend fun getSeasonData(
        language: String,
        silentMode: Boolean = false,
    ) {
        if (!silentMode) {
            loadScreenStateFlow.value = Container.Pending
        }

        try {
            val result = getSeasonUseCase.execute(
                language = language,
                seriesId = args.seriesId,
                seasonNumber = args.seasonNumber,
            )

            loadScreenStateFlow.value = Container.Success(result)

        } catch (e: Exception) {
            loadScreenStateFlow.value = Container.Error(e)
        }
    }

    fun updateSeason() = debounce {
        val loadState = loadScreenStateFlow.value

        if (loadState is Container.Success) {
            viewModelScope.launch {
                getSeasonData(
                    language = languageTag,
                    silentMode = true,
                )
            }
        }

    }

    @AssistedFactory
    interface Factory {
        fun create(
            args: SeasonDetailsBottomSheetDialogFragment.Screen,
        ): SeasonDetailsViewModel
    }

}