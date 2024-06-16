package com.ru.movieshows.tv_shows.presentation.top_rated

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.TvShowsRouter
import com.ru.movieshows.tv_shows.domain.GetPagedTopRatedTvShowsUseCase
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class TopRatedTvShowsViewModel @Inject constructor(
    private val router: TvShowsRouter,
    private val getPagedTopRatedTvShowsUseCase: GetPagedTopRatedTvShowsUseCase,
) : BaseViewModel(), SimpleAdapterListener<TvShow> {

    @OptIn(ExperimentalCoroutinesApi::class)
    val topRatedTvShows: Flow<PagingData<TvShow>> = languageTagFlow.flatMapLatest { language ->
        getPagedTopRatedTvShowsUseCase.execute(language)
    }.cachedIn(viewModelScope)

    override fun onClickItem(data: TvShow) = debounce {
        router.launchTvShowsDetails(data)
    }

}