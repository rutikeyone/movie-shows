package com.ru.movieshows.data.repository

import androidx.paging.PagingData
import com.ru.movieshows.domain.entity.CommentEntity
import com.ru.movieshows.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

interface YoutubeRepository : Repository {
    fun getPagedCommentsByVideo(videoId: String?) : Flow<PagingData<CommentEntity>>
}

