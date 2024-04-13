package com.ru.movieshows.splash.presentation.splash

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.splash.domain.HasCurrentSessionIdUseCase
import com.ru.movieshows.splash.domain.IsFirstLaunchUseCase
import com.ru.movieshows.splash.presentation.SplashRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashRouter: SplashRouter,
    private val hasCurrentSessionIdUseCase: HasCurrentSessionIdUseCase,
    private val isFirstLaunchUseCase: IsFirstLaunchUseCase,
) : BaseViewModel(), DefaultLifecycleObserver {

    private fun setupSettings() = debounce {
        viewModelScope.launch {
            val firstLaunch = isFirstLaunchUseCase.getFirstLaunch()
            val hasCurrentAccount = hasCurrentSessionIdUseCase.hasCurrentAccount()

            if (!firstLaunch || hasCurrentAccount) {
                splashRouter.launchMain()
            } else {
                splashRouter.launchSignIn()
            }
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        setupSettings()
    }

}