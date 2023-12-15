package com.ru.movieshows.presentation.sideeffects.navigator

import androidx.navigation.NavDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ru.movieshows.presentation.sideeffects.ResourceActions
import javax.inject.Inject

class IntermediateNavigator @Inject constructor() : NavigatorWrapper {

    override val targetNavigator: ResourceActions<Navigator> =  ResourceActions<Navigator>()

    override fun setTarget(navigator: Navigator?) {
        targetNavigator.resource = navigator
    }

    override fun clean() {
        targetNavigator.clear()
    }

    override fun authenticated() = targetNavigator { navigator ->
        navigator.authenticated()
    }

    override fun notAuthenticated() = targetNavigator { navigator ->
        navigator.notAuthenticated()
    }

    override fun navigate(direction: NavDirections) = targetNavigator { navigator ->
        navigator.navigate(direction)
    }

    override fun goBack() = targetNavigator { navigator ->
          navigator.goBack()
    }

    override fun setStartDestination() = targetNavigator { navigator ->
        navigator.setStartDestination()
    }

    override fun getBottomNavigationView(): BottomNavigationView? {
        return targetNavigator.resource?.getBottomNavigationView()
    }

}