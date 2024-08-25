package com.ru.movieshows.navigation.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes

class NavTab(
    @IdRes val destinationId: Int,
    @IdRes val fragmentId: Int,
    val title: Int,
    @DrawableRes val iconRes: Int,
) : java.io.Serializable