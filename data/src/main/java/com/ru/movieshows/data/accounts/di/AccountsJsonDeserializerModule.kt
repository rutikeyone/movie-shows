package com.ru.movieshows.data.accounts.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseImageUrl

@Module
@InstallIn(SingletonComponent::class)
class JsonDeserializerModule {

    @Binds
    @Singleton
    @BaseImageUrl
    fun bindBaseImageUrl(): String {
        return "https://image.tmdb.org/t/p/original"
    }

}
