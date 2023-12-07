package com.ru.movieshows.presentation.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.AccountRepository
import com.ru.movieshows.domain.entity.AuthenticatedState
import com.ru.movieshows.presentation.sideeffects.loader.LoaderOverlay
import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorWrapper
import com.ru.movieshows.presentation.sideeffects.resources.Resources
import com.ru.movieshows.presentation.sideeffects.toast.Toasts
import com.ru.movieshows.presentation.utils.Event
import com.ru.movieshows.presentation.utils.MutableLiveEvent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityScopeViewModel @Inject constructor(
    val navigator: NavigatorWrapper,
    val toasts: Toasts,
    val resources: Resources,
    private val accountsRepository: AccountRepository,
): ViewModel(), NavigatorWrapper by navigator, LoaderOverlay {

    private val _loaderOverlayState = MutableLiveEvent(Event(false))
    val loaderOverlayState = _loaderOverlayState.share()

    init {
        handleState()
    }

    private fun handleState() {

        viewModelScope.launch {
            accountsRepository.observeState()
        }

        viewModelScope.launch {
            accountsRepository.state.collect { state ->
                when (state) {
                    AuthenticatedState.Pure -> {}
                    is AuthenticatedState.InPending -> {
                        val isNotAuthenticated = state.isNotAuthenticated
                        if (!isNotAuthenticated) setStartDestination()
                        else showLoader()
                    }
                    is AuthenticatedState.Authenticated -> {
                        navigator.authenticated()
                        hideLoader()
                    }
                    is AuthenticatedState.NotAuthenticated -> {
                        navigator.notAuthenticated()
                        hideLoader()
                        state.error?.let {
                            val error = resources.getString(it.errorResource())
                            toasts.toast(error)
                        }
                    }
                }
            }
        }
    }

    override fun showLoader() = _loaderOverlayState.publishEvent(true)

    override fun hideLoader() = _loaderOverlayState.publishEvent(false)

    override fun onCleared() {
        super.onCleared()
        navigator.clean()
    }
}