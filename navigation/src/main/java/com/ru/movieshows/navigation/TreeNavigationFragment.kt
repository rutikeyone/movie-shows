package com.ru.movieshows.navigation

import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

interface TreeNavigationFragment {

    fun tabsToolbar(): Toolbar

    fun bottomNavigationView(): BottomNavigationView

}