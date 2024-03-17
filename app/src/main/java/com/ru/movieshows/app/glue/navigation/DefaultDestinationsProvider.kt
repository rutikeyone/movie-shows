package com.ru.movieshows.app.glue.navigation

import android.content.Context
import com.ru.movieshows.app.R
import com.ru.movieshows.navigation.DestinationsProvider
import com.ru.movieshows.navigation.presentation.navigation.NavTab
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultDestinationsProvider @Inject constructor(
    @ApplicationContext context: Context,
): DestinationsProvider {

    override fun provideStartDestinationId(): Int {
        return R.id.splashFragment
    }

    override fun provideNavigationGraphId(): Int {
        return R.navigation.nav_graph
    }

    override fun provideMainTabs(): List<NavTab> {
        return listOf()
    }

    override fun provideTabsDestinationId(): Int {
        return R.id.tabsFragment
    }

}