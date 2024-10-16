package com.ru.movieshows.navigation

import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import com.ru.movieshows.navigation.presentation.NavTab

interface DestinationsProvider {

    @IdRes
    fun provideStartDestinationId(): Int

    @IdRes
    fun provideStartAuthDestinationId(): Int

    @NavigationRes
    fun provideNavigationGraphId(): Int

    @NavigationRes
    fun provideTabNavigationGraphId(): Int

    fun provideMainTabs(): List<NavTab>

    @IdRes
    fun provideTabsDestinationId(): Int

}