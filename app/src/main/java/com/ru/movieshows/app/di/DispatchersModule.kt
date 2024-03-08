package com.ru.movieshows.app.di

import com.ru.movieshows.app.utils.dispatcher.Dispatcher
import com.ru.movieshows.app.utils.dispatcher.MainThreadDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class DispatchersModule {

    @Provides
    fun provideDispatcher(): Dispatcher {
        return MainThreadDispatcher()
    }

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

}
