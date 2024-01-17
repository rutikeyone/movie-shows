package com.ru.movieshows.app.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.app.di.SideEffectsModule
import com.ru.movieshows.app.model.accounts.AccountRepository
import com.ru.movieshows.app.presentation.sideeffects.loader.LoaderOverlay
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.sideeffects.resources.Resources
import com.ru.movieshows.app.presentation.sideeffects.toast.Toasts
import com.ru.movieshows.app.utils.share
import com.ru.movieshows.sources.accounts.entities.AuthStateEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ActivityScopeViewModel @Inject constructor(
    @Named(SideEffectsModule.INTERMEDIATE_NAVIGATOR_DEPENDENCY)
    val navigator: Navigator,
    val toasts: Toasts,
    val resources: Resources,
    private val accountsRepository: AccountRepository,
): ViewModel(), LoaderOverlay {

    private val _loaderOverlayState = MutableLiveData(false)
    val loaderOverlayState = _loaderOverlayState.share()

    init {
        viewModelScope.launch {
            accountsRepository.getState().collect { state ->
                handleAuthState(state)
            }
        }
    }

    private fun handleAuthState(state: AuthStateEntity) {
        when (state) {
            AuthStateEntity.Empty -> {}
            is AuthStateEntity.Pending -> handlePendingState(state)
            is AuthStateEntity.Auth -> handleAuthenticatedState()
            is AuthStateEntity.NotAuth -> handleNotAuthenticatedState(state)
        }
    }

    private fun handleAuthenticatedState() {
        val isNestedRoute = !navigator.isNestedRoute()
        if (isNestedRoute) navigator.authenticated()
        hideLoader()
    }

    private fun handlePendingState(state: AuthStateEntity.Pending) {
        val isNotAuth = state.isNotAuth
        if (!isNotAuth) {
            navigator.setStartDestination()
        }
        else {
            showLoader()
        }
    }

    private fun handleNotAuthenticatedState(state: AuthStateEntity.NotAuth) {
        val isPrevStateAuth = state.isPrevStateAuth
        if(!isPrevStateAuth) {
            navigator.notAuthenticated(state.isFirstLaunch)
        } else {
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
        super.onCleared()
        navigator.clean()
    }
}