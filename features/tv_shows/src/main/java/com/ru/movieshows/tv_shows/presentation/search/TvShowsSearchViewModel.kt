package com.ru.movieshows.tv_shows.presentation.search

import androidx.paging.cachedIn
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.TvShowsRouter
import com.ru.movieshows.tv_shows.domain.DeleteTvShowSearchUseCase
import com.ru.movieshows.tv_shows.domain.GetAllTvShowsSearchUseCase
import com.ru.movieshows.tv_shows.domain.InsertTvShowSearchUseCase
import com.ru.movieshows.tv_shows.domain.SearchPagedTvShowsUseCase
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tv_shows.domain.entities.TvShowSearch
import com.ru.movieshows.tv_shows.presentation.adapters.TvShowSearchListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TvShowsSearchViewModel @Inject constructor(
    private val searchPagedTvShowsUseCase: SearchPagedTvShowsUseCase,
    private val router: TvShowsRouter,
    private val insertTvShowSearchUseCase: InsertTvShowSearchUseCase,
    private val deleteTvShowSearchUseCase: DeleteTvShowSearchUseCase,
    private val getAllTvShowsSearchUseCase: GetAllTvShowsSearchUseCase,
) : BaseViewModel(), SimpleAdapterListener<TvShow>, TvShowSearchListener {

    val tvShowsSearchStateFlow: Flow<List<TvShowSearch>> = languageTagFlow.flatMapLatest {
        getAllTvShowsSearchUseCase.execute(it)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val queryStateFlow = MutableStateFlow("")

    private val queryStateLiveValue = queryStateFlow
        .toLiveValue("")

    private val nullableQueryStateFlow: Flow<String?> = queryStateFlow.map { value ->
        return@map value.ifEmpty {
            null
        }
    }

    val queryStateValue: String?
        get() {
            val queryValue = queryStateLiveValue.value
            return queryValue.ifEmpty {
                null
            }
        }

    val isSearchMode: Boolean
        get() = !queryStateValue.isNullOrEmpty()

    val searchTvShows = combine(
        languageTagFlow,
        nullableQueryStateFlow
    ) { language, query ->
        State(
            language = language,
            query = query,
        )
    }.flatMapLatest {
        searchPagedTvShowsUseCase.execute(it.language, it.query)
    }.cachedIn(viewModelScope)

    fun changeQuery(query: String) {
        if (queryStateValue != query) {
            queryStateFlow.value = query
        }
    }

    override fun onClickItem(data: TvShow) = debounce {
        val locale: String = languageTag

        viewModelScope.launch {
            insertTvShowSearchUseCase.execute(data, locale)
            router.launchTvShowsDetails(data.id)
        }
    }

    override fun onDeleteTvShowSearch(tvShowSearch: TvShowSearch) = debounce {
        viewModelScope.launch {
            deleteTvShowSearchUseCase.execute(tvShowSearch.id)
        }
    }

    override fun onSelectTvShowSearch(tvShowSearch: TvShowSearch) = debounce {
        val id = tvShowSearch.tvShowId ?: return@debounce
        router.launchTvShowsDetails(id)
    }

    data class State(
        val language: String,
        val query: String?,
    )

}