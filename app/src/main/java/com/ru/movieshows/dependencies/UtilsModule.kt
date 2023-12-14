package com.ru.movieshows.dependencies

import com.ru.movieshows.presentation.utils.dispatcher.MainThreadDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    @Provides
    fun provideMainThreadDispatcher(): MainThreadDispatcher {
        return MainThreadDispatcher()
    }

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

}