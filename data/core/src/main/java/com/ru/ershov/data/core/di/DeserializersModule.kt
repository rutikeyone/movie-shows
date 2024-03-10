package com.ru.ershov.data.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseImageUrlQualifier

@Module
@InstallIn(SingletonComponent::class)
interface DeserializersModule {

    @Binds
    @Singleton
    @BaseImageUrlQualifier
    fun bindBaseImageUrl(): String {
        return "https://image.tmdb.org/t/p/original"
    }

}