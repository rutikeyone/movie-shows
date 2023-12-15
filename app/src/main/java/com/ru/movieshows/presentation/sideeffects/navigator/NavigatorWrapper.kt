package com.ru.movieshows.presentation.sideeffects.navigator

import androidx.navigation.NavDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ru.movieshows.presentation.sideeffects.ResourceActions

interface NavigatorWrapper {

    val targetNavigator: ResourceActions<Navigator>

    fun authenticated()

    fun notAuthenticated()

    fun navigate(direction: NavDirections)

    fun goBack()

    fun setStartDestination()

    fun getBottomNavigationView(): BottomNavigationView?

    fun setTarget(navigator: Navigator?)

    fun clean()

}