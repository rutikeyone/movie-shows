package com.ru.movieshows.presentation.viewmodel.tv_show_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.SeasonEntity
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeasonDetailsViewModel @Inject constructor(
    val tvShowRepository: TvShowRepository,
) : BaseViewModel() {
    private var seasonNumber: String? = null
    private var seriesId: String? = null

    private var _season = MutableLiveData<SeasonEntity?>(null)
    val season = _season.share()

   private fun getSeason(seriesId: String?, seasonNumber: String?) = viewModelScope.launch {
       val language = languageTag
       val seriesId = seriesId ?: return@launch
       val seasonNumber = seasonNumber ?: return@launch
       val season = tvShowRepository.getSeason(language, seriesId, seasonNumber)
       val result = season.getOrNull() ?: return@launch
       if(_season.value == result) return@launch
       _season.value = result
   }

    fun updateData(
        season: SeasonEntity?,
        seasonNumber: String?,
        seriesId: String?,
    ) {
        this.seasonNumber = seasonNumber
        this.seriesId = seriesId
        _season.value = season
        getSeason(seriesId, seasonNumber)
    }

}