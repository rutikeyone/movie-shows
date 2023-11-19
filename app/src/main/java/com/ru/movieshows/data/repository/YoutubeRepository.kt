package com.ru.movieshows.data.repository

import androidx.paging.PagingData
import com.ru.movieshows.domain.entity.CommentEntity
import kotlinx.coroutines.flow.Flow

interface YoutubeRepository {
    fun getPagedCommentsByVideo(videoId: String?) : Flow<PagingData<CommentEntity>>
}

