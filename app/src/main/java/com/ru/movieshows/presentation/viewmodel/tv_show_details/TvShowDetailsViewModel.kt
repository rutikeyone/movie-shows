package com.ru.movieshows.presentation.viewmodel.tv_show_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.movie_details.MovieDetailsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

class TvShowDetailsViewModel @AssistedInject constructor(
    @Assisted private val movieId: String,
) : BaseViewModel() {
    private val _title = MutableLiveData<String?>("")
    val title = _title.share()

    @AssistedFactory
    interface Factory {
        fun create(movieId: String): TvShowDetailsViewModel
    }
}