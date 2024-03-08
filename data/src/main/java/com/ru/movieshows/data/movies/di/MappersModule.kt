package com.ru.movieshows.data.movies.di

import com.ru.movieshows.data.movies.mappers.PreviewMapper
import com.ru.movieshows.data.movies.mappers.YoutubePreviewMapper
import dagger.Binds
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Singleton
@InstallIn(Singleton::class)
interface MappersModule {

    @Binds
    @Singleton
    fun bindPreviewMapper(
        previewMapper: YoutubePreviewMapper
    ): PreviewMapper

}