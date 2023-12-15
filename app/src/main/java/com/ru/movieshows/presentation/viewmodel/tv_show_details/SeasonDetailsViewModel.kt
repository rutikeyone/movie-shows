package com.ru.movieshows.presentation.viewmodel.tv_show_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.SeasonEntity
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.share
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

   fun updateData() = viewModelScope.launch {
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