package com.ru.movieshows.presentation.screens.movie_reviews

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ru.movieshows.domain.entity.ReviewEntity

typealias ReviewsPageLoader = suspend (pageIndex: Int) -> Pair<List<ReviewEntity>, Int>
class ReviewsPagingSource(
    private val loader: ReviewsPageLoader,
    private val pageSize: Int,
) : PagingSource<Int, ReviewEntity>() {
    override fun getRefreshKey(state: PagingState<Int, ReviewEntity>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewEntity> {
        val pageIndex = params.key ?: 1
        return try {
            val loaderResult = loader.invoke(pageIndex)
            return LoadResult.Page(
                data = loaderResult.first,
                prevKey = if (pageIndex == 1) null else pageIndex - 1,
                nextKey = if(loaderResult.second > pageIndex)  pageIndex + 1 else null
                //nextKey = null,
            )
            } catch (e: Exception) {
            LoadResult.Error(throwable = e)
        }
    }

}