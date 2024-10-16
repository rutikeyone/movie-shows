package com.ru.movieshows.navigation.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes

class NavTab(
    @IdRes val destinationId: Int,
    @IdRes val fragmentId: Int,
    val title: Int,
    @DrawableRes val iconRes: Int,
) : java.io.Serializable