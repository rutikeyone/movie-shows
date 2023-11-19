package com.ru.movieshows.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ru.movieshows.data.dto.YoutubeDto
import com.ru.movieshows.data.repository.YoutubeRepository
import com.ru.movieshows.domain.entity.CommentEntity
import com.ru.movieshows.domain.utils.AppFailure
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject

class YoutubeRepositoryImpl @Inject constructor(
    private val youtubeDto: YoutubeDto,
) : YoutubeRepository {

    override fun getPagedCommentsByVideo(videoId: String?): Flow<PagingData<CommentEntity>> {

        val loader = object : PagingSource<String, CommentEntity>() {
            override fun getRefreshKey(state: PagingState<String, CommentEntity>): String? {
                return state.anchorPosition?.let { state.closestItemToPosition(it)?.id }
            }

            override suspend fun load(params: LoadParams<String>): LoadResult<String, CommentEntity> {
                return try {
                    val response = youtubeDto.getCommentsByVideo(videoId = videoId, pageToken = params.key, part = "snippet,replies")
                    val result = response.items?.map { it.toEntity() } ?: listOf()

                    return LoadResult.Page(
                        data = result,
                        prevKey = params.key,
                        nextKey = response.nextPageToken,
                    )

                }  catch (e: ConnectException) {
                    val failure = AppFailure.Connection
                    LoadResult.Error(failure)
                }
                catch (e: HttpException) {
                    val statusCode = e.code()

                    if(statusCode == 404) {
                        val data = listOf<CommentEntity>()
                        return LoadResult.Page(data, null, null)
                    }

                    val failure = AppFailure.Pure
                    LoadResult.Error(failure)
                }
                catch (e: Exception) {
                    val failure = AppFailure.Pure
                    LoadResult.Error(failure)
                }
            }

        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { loader }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}