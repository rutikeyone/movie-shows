package com.ru.movieshows.app.glue.navigation

import android.content.Context
import com.ru.movieshows.app.R
import com.ru.movieshows.navigation.DestinationsProvider
import com.ru.movieshows.navigation.presentation.NavTab
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultDestinationsProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) : DestinationsProvider {

    override fun provideStartDestinationId(): Int {
        return R.id.splashFragment
    }

    override fun provideStartAuthDestinationId(): Int {
        return R.id.signInFragment
    }

    override fun provideNavigationGraphId(): Int {
        return R.navigation.app_graph
    }

    override fun provideTabNavigationGraphId(): Int {
        return R.navigation.tabs_graph
    }

    override fun provideMainTabs(): List<NavTab> {
        return listOf(
            NavTab(
                destinationId = R.id.movies_tab,
                title = R.string.movies,
                fragmentId = R.id.moviesFragment,
                iconRes = R.drawable.ic_movie,
            ),
            NavTab(
                destinationId = R.id.tv_show_tab,
                title = R.string.tvs,
                fragmentId = R.id.tvShowsFragment,
                iconRes = R.drawable.ic_tv,
            ),
            NavTab(
                destinationId = R.id.profile_tab,
                title = R.string.profile,
                fragmentId = R.id.profileFragment,
                iconRes = R.drawable.ic_profile,
            ),
        )
    }

    override fun provideTabsDestinationId(): Int {
        return R.id.tabsFragment
    }

}