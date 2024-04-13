package com.ru.movieshows.navigation

import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import com.ru.movieshows.navigation.presentation.navigation.NavTab

interface DestinationsProvider {

    @IdRes
    fun provideStartDestinationId(): Int

    @IdRes
    fun provideStartAuthDestinationId(): Int

    @NavigationRes
    fun provideNavigationGraphId(): Int

    fun provideMainTabs(): List<NavTab>

    @IdRes
    fun provideTabsDestinationId(): Int

}