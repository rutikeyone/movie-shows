package com.ru.movieshows.presentation.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.AccountRepository
import com.ru.movieshows.domain.entity.AccountEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorWrapper
import com.ru.movieshows.presentation.utils.MutableLiveEvent
import com.ru.movieshows.presentation.utils.ToastIntent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityScopeViewModel @Inject constructor(
    val navigator: NavigatorWrapper,
    private val accountsRepository: AccountRepository,
): ViewModel(), NavigatorWrapper by navigator {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Pure)
    val authState: StateFlow<AuthState> get() = _authState

    private val toastEvent = MutableLiveEvent<ToastIntent>()
    val toastShareEvent = toastEvent.share()

    init {
        getAccount()
    }

    private fun getAccount() = viewModelScope.launch {
        _authState.value = AuthState.InPending
        navigator.setStartDestination()
        accountsRepository.getAccount().collect { result ->
            result.fold(::onFailure, ::onSuccess)
        }
    }

    private fun onSuccess(account: AccountEntity?) {
        if(account != null) {
            val authenticatedState = AuthState.Authenticated(account)
            _authState.value = authenticatedState
            navigator.authenticated()
        } else {
            val authenticatedState = AuthState.NotAuthenticated
            _authState.value = authenticatedState
            navigator.notAuthenticated()
        }
    }

    private fun onFailure(appFailure: AppFailure) {
        val state = AuthState.NotAuthenticated
        val toastIntent = ToastIntent(appFailure.errorResource())
        _authState.value = state
        toastEvent.publishEvent(toastIntent)
        navigator.notAuthenticated()
    }

    override fun onCleared() {
        super.onCleared()
        navigator.clean()
    }
}