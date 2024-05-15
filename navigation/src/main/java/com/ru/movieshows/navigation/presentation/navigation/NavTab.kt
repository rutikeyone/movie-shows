package com.ru.movieshows.navigation.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes

class NavTab(
    @IdRes val destinationId: Int,
    val title: Int,
    @DrawableRes val iconRes: Int,
) : java.io.Serializable