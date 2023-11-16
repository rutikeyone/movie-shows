package com.ru.movieshows.presentation.viewmodel.video

import com.ru.movieshows.domain.entity.VideoEntity
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class VideoViewModel  @AssistedInject constructor(
    @Assisted val video: VideoEntity,
) : BaseViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(video: VideoEntity): VideoViewModel
    }

}