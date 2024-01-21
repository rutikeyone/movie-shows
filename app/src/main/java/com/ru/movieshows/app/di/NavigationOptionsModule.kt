package com.ru.movieshows.app.di

import com.ru.movieshows.AppGraphDirections
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.sideeffects.navigator.NavigationOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NavigationOptionsModule {

    @Provides
    @Singleton
    fun provideNavigationOptions(): NavigationOptions {
        return NavigationOptions(
            fragmentContainer = R.id.fragmentContainer,
            appGraph = R.navigation.app_graph,
            splashDestination = R.id.splashFragment,
            toSignIn = AppGraphDirections.actionGlobalSignInFragment(),
            toTabs = AppGraphDirections.actionGlobalTabsFragment(),
            topLevelDestinations = setOf(R.id.moviesFragment, R.id.tvsFragment, R.id.profileFragment),
            tabFragmentId = R.id.tabs_fragment,
        )
    }

}