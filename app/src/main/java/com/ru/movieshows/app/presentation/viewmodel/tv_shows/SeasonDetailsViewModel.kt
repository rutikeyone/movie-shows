package com.ru.movieshows.app.presentation.viewmodel.tv_shows

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.app.model.tv_shows.TvShowRepository
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.app.utils.share
import com.ru.movieshows.sources.tv_shows.entities.SeasonEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch


class SeasonDetailsViewModel @AssistedInject constructor(
    @Assisted private val arguments: Pair<String, String>,
    @Assisted private val initialSeason: SeasonEntity,
    private val tvShowRepository: TvShowRepository,
) : BaseViewModel() {

    private var _season = MutableLiveData<SeasonEntity?>(null)
    val season = _season.share()

    init {
        _season.value = initialSeason
    }

   fun update() = viewModelScope.launch {
       val language = languageTag
       val season = tvShowRepository.getSeason(language, arguments.second, arguments.first)
       val result = season.getOrNull()
       if(result != null && _season.value != result) {
           _season.value = result
       }
   }

    @AssistedFactory
    interface Factory {
        fun create(
            arguments: Pair<String, String>,
            initialSeason: SeasonEntity,
        ): SeasonDetailsViewModel
    }

}