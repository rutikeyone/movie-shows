package com.ru.movieshows.core.presentation

import com.google.android.material.tabs.TabLayout

abstract class TabSelectedListener : TabLayout.OnTabSelectedListener {

    abstract override fun onTabSelected(tab: TabLayout.Tab?)

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {}

}