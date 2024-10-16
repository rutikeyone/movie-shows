package com.ru.movieshows.app.model

import androidx.paging.PagingSource
import androidx.paging.PagingState

typealias PageLoader<T> = suspend (pageIndex: Int) -> Pair<T, Int>

class PagePagingSource<T : Any>(
    private val loader: PageLoader<List<T>>,
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val pageIndex = params.key ?: 1
        return try {
            val loaderResult = loader.invoke(pageIndex)
            return LoadResult.Page(
                data = loaderResult.first,
                prevKey = if (pageIndex == 1) null else pageIndex - 1,
                nextKey = if (loaderResult.second > pageIndex) pageIndex + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(throwable = e)
        }
    }

}