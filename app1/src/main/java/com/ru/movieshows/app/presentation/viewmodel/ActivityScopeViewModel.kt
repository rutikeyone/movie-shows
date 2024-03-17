package com.ru.movieshows.app.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.app.di.NavigatorWrapper
import com.ru.movieshows.app.di.SideEffectsModule
import com.ru.movieshows.app.model.accounts.AccountRepository
import com.ru.movieshows.app.presentation.sideeffects.loader.LoaderOverlay
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.sideeffects.resources.Resources
import com.ru.movieshows.app.presentation.sideeffects.toast.Toasts
import com.ru.movieshows.app.utils.share
import com.ru.movieshows.sources.accounts.entities.UserAuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ActivityScopeViewModel @Inject constructor(
    @NavigatorWrapper val navigator: Navigator,
    val toasts: Toasts,
    val resources: Resources,
    private val accountsRepository: AccountRepository,
): ViewModel(), LoaderOverlay {

    private val _loaderOverlayState = MutableLiveData(false)
    val loaderOverlayState = _loaderOverlayState.share()

    init {
        viewModelScope.launch {
            accountsRepository.getUserAuthenticationState().collect { state ->
                handleAuthState(state)
            }
        }
    }

    private fun handleAuthState(state: UserAuthenticationState) {
        when (state) {
            UserAuthenticationState.Empty -> {}
            is UserAuthenticationState.Pending -> handlePendingState(state)
            is UserAuthenticationState.Authentication -> handleAuthenticatedState()
            is UserAuthenticationState.NotAuthentication -> handleNotAuthenticatedState(state)
        }
    }

    private fun handleAuthenticatedState() {
        val isNestedRoute = !navigator.isNestedRoute()
        if (isNestedRoute) navigator.authenticated()
        hideLoader()
    }

    private fun handlePendingState(state: UserAuthenticationState.Pending) {
        val isNotAuthenticated = state.previousNotAuthenticated
        if (!isNotAuthenticated) {
            navigator.setStartDestination()
        } else {
            showLoader()
        }
    }

    private fun handleNotAuthenticatedState(state: UserAuthenticationState.NotAuthentication) {
        val previousStateAuthenticated = state.previousAuthenticated
        if(state.firstLaunch) {
            navigator.notAuthenticated(true)
        } else if(!previousStateAuthenticated) {
            navigator.authenticated()
        }
        hideLoader()
    }

    override fun showLoader() {
        _loaderOverlayState.value = true
    }

    override fun hideLoader() {
        _loaderOverlayState.value = false
    }

    override fun onCleared() {
        navigator.clean()
        super.onCleared()
    }

}