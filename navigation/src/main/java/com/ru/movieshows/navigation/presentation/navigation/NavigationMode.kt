package com.ru.movieshows.navigation.presentation.navigation

sealed class NavigationMode: java.io.Serializable {

    object Stack: NavigationMode()

    class Tabs(
        val tabs: List<NavTab>,
        val startTabsDestinationId: Int?,
    ): NavigationMode()

}