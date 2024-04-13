package com.ru.movieshows.app.formatters.di

import com.ru.movieshows.app.formatters.DefaultImageUrlFormatter
import com.ru.movieshows.app.formatters.ImageUrlFormatter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FormattersModule {

    @Binds
    fun bindImageUrlFormatter(
        imageUrlFormatter: DefaultImageUrlFormatter,
    ): ImageUrlFormatter

}