package com.ru.movieshows.app.glue.navigation

import android.content.Context
import com.ru.movieshows.app.R
import com.ru.movieshows.navigation.DestinationsProvider
import com.ru.movieshows.navigation.presentation.navigation.NavTab
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
        return R.navigation.nav_graph
    }

    override fun provideMainTabs(): List<NavTab> {
        return listOf(
            NavTab(
                destinationId = R.id.moviesFragment,
                title = context.getString(R.string.movies),
                iconRes = R.drawable.ic_movie,
            ),
            NavTab(
                destinationId = R.id.tvShowsFragment,
                title = context.getString(R.string.tvs),
                iconRes = R.drawable.ic_tv,
            ),
            NavTab(
                destinationId = R.id.profileFragment,
                title = context.getString(R.string.profile),
                iconRes = R.drawable.ic_profile,
            ),
        )
    }

    override fun provideTabsDestinationId(): Int {
        return R.id.tabsFragment
    }

}