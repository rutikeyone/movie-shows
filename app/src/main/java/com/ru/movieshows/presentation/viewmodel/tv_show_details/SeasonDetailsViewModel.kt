package com.ru.movieshows.presentation.viewmodel.tv_show_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.SeasonEntity
import com.ru.movieshows.presentation.screens.tv_show_details.SeasonDetailsBottomSheetDialogFragmentDirections
import com.ru.movieshows.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.share
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch


class SeasonDetailsViewModel @AssistedInject constructor(
    @Assisted private val arguments: Pair<String, String>,
    @Assisted private val initialSeason: SeasonEntity,
    @Assisted private val navigator: Navigator,
    private val tvShowRepository: TvShowRepository,
) : BaseViewModel() {

    private var _season = MutableLiveData<SeasonEntity?>(null)
    val season = _season.share()

    init {
        _season.value = initialSeason
    }

   fun updateData() = viewModelScope.launch {
       val language = languageTag
       val season = tvShowRepository.getSeason(language, arguments.second, arguments.first)
       val result = season.getOrNull()
       if(result != null && _season.value != result) {
           _season.value = result
       }
   }

    fun navigateToEpisodes() {
        val seriesId = arguments.second
        val seasonNumber = arguments.first
        val direction = SeasonDetailsBottomSheetDialogFragmentDirections
        val action = direction.actionSeasonDetailsBottomSheetDialogFragmentToSeasonEpisodesFragment(
            seasonNumber = seasonNumber,
            seriesId = seriesId,
        )
        navigator.navigate(action)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            arguments: Pair<String, String>,
            initialSeason: SeasonEntity,
            navigator: Navigator,
        ): SeasonDetailsViewModel
    }

}