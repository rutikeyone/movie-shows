package com.ru.movieshows.presentation.viewmodel.tv_show_search

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.screens.tv_show_search.TvShowSearchFragmentDirections
import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorWrapper
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.share
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class TvShowSearchViewModel @AssistedInject constructor(
    @Assisted private val navigator: NavigatorWrapper,
    private val tvShowRepository: TvShowRepository,
) : BaseViewModel() {
    private val state = MediatorLiveData<Pair<String, String>>()

    private var _query = MutableLiveData("")
    val query = _query.share()

    val queryValue get() = if(_query.value.isNullOrEmpty()) null else _query.value
    val searchQueryMode: Boolean get() = !_query.value.isNullOrEmpty()

    val searchTvShows = state.asFlow().flatMapLatest {
        tvShowRepository
            .searchPagedMovies(it.second, it.first)
    }.cachedIn(viewModelScope)

    init {
        configureMediatorState()
    }

    private fun configureMediatorState() {
        state.addSource(_query) { query ->
            val languageTag = languageTagState.value ?: return@addSource
            state.value = Pair(query, languageTag)
        }

        state.addSource(languageTagState) { languageTag ->
            val query = _query.value ?: return@addSource
            state.value = Pair(query, languageTag)
        }
    }

    fun changeQuery(query: String) {
        if(queryValue != query) {
            _query.postValue(query)
        }
    }

    fun navigateToTvShowDetails(tvShow: TvShowsEntity) {
        val id = tvShow.id ?: return
        val action = TvShowSearchFragmentDirections.actionTvShowSearchFragmentToTvShowDetailsFragment(id)
        navigator.navigate(action)
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: NavigatorWrapper): TvShowSearchViewModel
    }

}