package com.ru.movieshows.presentation.viewmodel.video

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.YoutubeRepository
import com.ru.movieshows.domain.entity.CommentEntity
import com.ru.movieshows.domain.entity.VideoEntity
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class VideoViewModel  @AssistedInject constructor(
    @Assisted val video: VideoEntity,
    private val youtubeRepository: YoutubeRepository,
) : BaseViewModel() {

    val comments: Flow<PagingData<CommentEntity>> = languageTagFlow.flatMapLatest {
        youtubeRepository
            .getPagedCommentsByVideo(video.key)
        }.cachedIn(viewModelScope)

    @AssistedFactory
    interface Factory {
        fun create(video: VideoEntity): VideoViewModel
    }

}