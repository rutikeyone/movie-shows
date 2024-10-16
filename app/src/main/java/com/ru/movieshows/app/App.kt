package com.ru.movieshows.app

import android.app.Application
import com.ru.movieshows.core.Core
import com.ru.movieshows.core.CoreProvider
import com.ru.movieshows.navigation.domain.HandleFirstLaunchUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var coreProvider: CoreProvider

    @Inject
    lateinit var handleFirstLaunchUseCase: HandleFirstLaunchUseCase

    override fun onCreate() {
        super.onCreate()
        Core.init(coreProvider)
        handleFirstLaunchUseCase.execute()
    }

}