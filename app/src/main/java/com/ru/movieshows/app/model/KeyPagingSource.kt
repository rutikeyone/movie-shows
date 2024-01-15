package com.ru.movieshows.app.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
typealias KeyPageLoader<T> = suspend (key: String?) -> KeyResponseEntity<T>

data class KeyResponseEntity<T>(
    val items: List<T>,
    val nextKey: String?,
)

abstract class KeyEntity(
    open var key: String?,
)

class KeyPagingSource<T : KeyEntity>(
    private val loader: KeyPageLoader<T>,
): PagingSource<String, T>() {

    override fun getRefreshKey(state: PagingState<String, T>): String? {
        return state.anchorPosition?.let { state.closestItemToPosition(it)?.key }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, T> {

        return try {
            val response = loader(params.key)

            val items = response.items.onEach { item ->
                item.key = params.key
            }

            return LoadResult.Page(
                data = items,
                prevKey = params.key,
                nextKey = response.nextKey,
            )

        }  catch (e: AppFailure) {
            LoadResult.Error(e)
        }
        catch (e: Exception) {
            LoadResult.Error(AppFailure.Empty)
        }
    }
}