package com.ru.movieshows.dependencies

import androidx.navigation.NavDirections
import com.ru.movieshows.AppGraphDirections
import com.ru.movieshows.R
import com.ru.movieshows.presentation.sideeffects.navigator.IntermediateNavigator
import com.ru.movieshows.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorImpl
import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
class NavigatorModule {

    @Provides
    fun provideIntermediateNavigator(): NavigatorWrapper {
        return IntermediateNavigator()
    }

    @Provides
    @Named(fragmentContainer)
    fun provideFragmentContainer(): Int {
        return R.id.fragmentContainer
    }

    @Provides
    @Named(appGraph)
    fun provideAppGraph(): Int {
        return R.navigation.app_graph
    }

    @Provides
    @Named(splashFragment)
    fun provideSplashFragment(): Int {
        return R.id.splashFragment
    }

    @Provides
    @Named(globalTabsAction)
    fun provideGlobalTabsAction() : NavDirections {
        return AppGraphDirections.actionGlobalTabsFragment()
    }

    @Provides()
    @Named(globalSignIn)
    fun provideGlobalSignInFragment(): NavDirections {
        return AppGraphDirections.actionGlobalSignInFragment()
    }

    @Provides
    fun provideNavigatorImpl(): Navigator {
        return NavigatorImpl(
            fragmentContainer = provideFragmentContainer(),
            appGraph = provideAppGraph(),
            splashDestination = provideSplashFragment(),
            toGlobalSignIn = provideGlobalSignInFragment(),
            toTabs = provideGlobalTabsAction(),
            topLevelDestinations = provideTopLevelDestinations(),
        )
    }

    @Provides
    @Named(topLevelDestinations)
    fun provideTopLevelDestinations(): Set<Int> {
        return setOf(R.id.moviesFragment, R.id.tvsFragment, R.id.profileFragment)
    }


    companion object {
        const val fragmentContainer = "FRAGMENT_CONTAINER_DEPENDENCY"
        const val appGraph = "APP_GRAPH_DEPENDENCY"
        const val splashFragment = "SPLASH_FRAGMENT_DEPENDENCY"
        const val globalTabsAction = "GLOBAL_TABS_ACTION_DEPENDENCY"
        const val globalSignIn = "GLOBAL_SIGN_IN_DEPENDENCY"
        const val topLevelDestinations = "TOP_LEVEL_DESTINATION_DEPENDENCY"
    }

}

