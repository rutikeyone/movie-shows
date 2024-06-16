package com.ru.movieshows.tv_shows.presentation.popular

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.TvShowsRouter
import com.ru.movieshows.tv_shows.domain.GetPagedPopularTvShowsUseCase
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class PopularTvShowsViewModel @Inject constructor(
    private val router: TvShowsRouter,
    private val getPagedPopularTvShowsUseCase: GetPagedPopularTvShowsUseCase,
) : BaseViewModel(), SimpleAdapterListener<TvShow> {

    @OptIn(ExperimentalCoroutinesApi::class)
    val popularTvShows: Flow<PagingData<TvShow>> = languageTagFlow.flatMapLatest { language ->
        getPagedPopularTvShowsUseCase.execute(language)
    }.cachedIn(viewModelScope)

    override fun onClickItem(data: TvShow) = debounce {
        router.launchTvShowsDetails(data)
    }

}