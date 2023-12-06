package com.ru.movieshows.presentation.sideeffects.navigator

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavDirections
import com.google.android.material.bottomnavigation.BottomNavigationView

interface Navigator {

    fun authenticated()

    fun notAuthenticated()

    fun navigate(direction: NavDirections)

    fun goBack()

    fun setStartDestination()

    fun getToolbar(): Toolbar?

    fun getBottomNavigationView(): BottomNavigationView?

    fun injectActivity(activity: AppCompatActivity)

}