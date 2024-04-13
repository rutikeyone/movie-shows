package com.ru.movieshows.navigation.presentation.navigation

import androidx.navigation.NavController
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class NavigationModeHolder @Inject constructor() {

    var navigationMode: NavigationMode = NavigationMode.Stack

}