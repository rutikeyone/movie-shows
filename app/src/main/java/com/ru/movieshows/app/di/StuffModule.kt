package com.ru.movieshows.app.di

import com.ru.movieshows.AppGraphDirections
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.sideeffects.navigator.NavigationOptions
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
class StuffModule {

    @Provides
    fun provideDispatcher(): Dispatcher {
        return MainThreadDispatcher()
    }

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Provides
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
