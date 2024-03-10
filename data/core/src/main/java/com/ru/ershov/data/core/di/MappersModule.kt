package com.ru.ershov.data.core.di

import com.ru.ershov.data.core.ImagePreviewMapper
import com.ru.ershov.data.core.mappers.DefaultImagePreviewMapper
import com.ru.ershov.data.core.mappers.YoutubeImagePreviewMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class YoutubeImagePreviewMapperQualifier

@Module
@InstallIn(SingletonComponent::class)
interface MappersModule {

    @Binds
    @Singleton
    @YoutubeImagePreviewMapperQualifier
    fun bindYoutubeImagePreviewMapper(
        youtubeImagePreviewMapper: YoutubeImagePreviewMapper,
    ): ImagePreviewMapper

    @Binds
    @Singleton
    fun bindDefaultImagePreviewMapper(
        defaultImagePreviewMapper: DefaultImagePreviewMapper,
    ):  ImagePreviewMapper

}