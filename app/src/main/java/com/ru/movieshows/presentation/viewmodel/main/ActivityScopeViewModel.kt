package com.ru.movieshows.presentation.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.AccountRepository
import com.ru.movieshows.domain.entity.AuthenticatedState
import com.ru.movieshows.presentation.sideeffects.loader.LoaderOverlay
import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorWrapper
import com.ru.movieshows.presentation.sideeffects.resources.Resources
import com.ru.movieshows.presentation.sideeffects.toast.Toasts
import com.ru.movieshows.presentation.viewmodel.share
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

    private val _loaderOverlayState = MutableLiveData(false)
    val loaderOverlayState = _loaderOverlayState.share()

    init {
        handleState()
    }

    private fun handleState() = viewModelScope.launch {

        launch {
           accountsRepository.listenAuthenitationState()
        }

        launch {
            accountsRepository.state.collect {
                handleAuthenticationState(it)
            }
        }

    }

    private fun handleAuthenticationState(state: AuthenticatedState) {
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
                    val errorResource = it.errorResource()
                    val error = resources.getString(errorResource)
                    toasts.toast(error)
                }
            }
        }
    }

    override fun showLoader() {
        _loaderOverlayState.value = true
    }

    override fun hideLoader() {
        _loaderOverlayState.value = false
    }

    override fun onCleared() {
        super.onCleared()
        navigator.clean()
    }
}