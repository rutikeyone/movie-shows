package com.ru.movieshows.dependencies

import androidx.navigation.NavDirections
import com.ru.movieshows.AppGraphDirections
import com.ru.movieshows.R
import com.ru.movieshows.presentation.sideeffects.navigator.IntermediateNavigator
import com.ru.movieshows.presentation.sideeffects.navigator.NavComponentNavigator
import com.ru.movieshows.presentation.sideeffects.navigator.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
class NavigatorModule {

    @Provides
    fun provideIntermediateNavigator(): Navigator {
        return IntermediateNavigator()
    }

    @Provides
    @Named(fragmentContainerDependency)
    fun provideFragmentContainer(): Int {
        return R.id.fragmentContainer
    }

    @Provides
    @Named(appGraphDependency)
    fun provideAppGraph(): Int {
        return R.navigation.app_graph
    }

    @Provides
    @Named(splashFragmentDependency)
    fun provideSplashFragment(): Int {
        return R.id.splashFragment
    }

    @Provides
    @Named(globalTabsActionDependency)
    fun provideGlobalTabsAction() : NavDirections {
        return AppGraphDirections.actionGlobalTabsFragment()
    }

    @Provides()
    @Named(globalSignInDependency)
    fun provideGlobalSignInFragment(): NavDirections {
        return AppGraphDirections.actionGlobalSignInFragment()
    }

    @Provides
    @Named(navigatorImplDependency)
    fun provideNavComponentNavigator(): Navigator {
        return NavComponentNavigator(
            fragmentContainer = provideFragmentContainer(),
            appGraph = provideAppGraph(),
            splashDestination = provideSplashFragment(),
            toGlobalSignIn = provideGlobalSignInFragment(),
            toTabs = provideGlobalTabsAction(),
            topLevelDestinations = provideTopLevelDestinations(),
        )
    }

    @Provides
    @Named(topLevelDestinationsDependency)
    fun provideTopLevelDestinations(): Set<Int> {
        return setOf(R.id.moviesFragment, R.id.tvsFragment, R.id.profileFragment)
    }


    companion object {
        const val fragmentContainerDependency = "FRAGMENT_CONTAINER_DEPENDENCY"
        const val appGraphDependency = "APP_GRAPH_DEPENDENCY"
        const val splashFragmentDependency = "SPLASH_FRAGMENT_DEPENDENCY"
        const val globalTabsActionDependency = "GLOBAL_TABS_ACTION_DEPENDENCY"
        const val globalSignInDependency = "GLOBAL_SIGN_IN_DEPENDENCY"
        const val topLevelDestinationsDependency = "TOP_LEVEL_DESTINATION_DEPENDENCY"
        const val navigatorImplDependency = "NAVIGATOR_IMPL_DEPENDENCY"
    }

}

