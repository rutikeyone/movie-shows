package com.ru.movieshows.navigation.presentation

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.navigation.domain.SetFirstLaunchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val setFirstLaunchUseCase: SetFirstLaunchUseCase,
) : BaseViewModel(), DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        setupFirstLaunch()
    }

    private fun setupFirstLaunch() {
        setFirstLaunchUseCase.setupLaunch()
    }

}