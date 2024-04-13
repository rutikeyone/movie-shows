package com.ru.movieshows.navigation.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes

class NavTab(
    @IdRes val destinationId: Int,
    val title: String,
    @DrawableRes val iconRes: Int,
) : java.io.Serializable