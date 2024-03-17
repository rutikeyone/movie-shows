package com.ru.movieshows.navigation.di

import com.ru.movieshows.core.AppRestarter
import com.ru.movieshows.impl.ActivityRequired
import com.ru.movieshows.navigation.GlobalNavComponentRouter
import com.ru.movieshows.navigation.MainAppRestarter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
class NavigationModule {

    @Provides
    fun provideAppRestarter(
        appRestarter: MainAppRestarter
    ): AppRestarter {
        return appRestarter
    }


    @Provides
    @IntoSet
    fun provideRouterAsActivityRequired(
        router: GlobalNavComponentRouter,
    ): ActivityRequired {
        return router
    }

}