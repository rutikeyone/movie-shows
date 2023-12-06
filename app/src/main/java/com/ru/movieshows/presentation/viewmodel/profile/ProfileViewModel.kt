package com.ru.movieshows.presentation.viewmodel.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.AccountRepository
import com.ru.movieshows.domain.entity.AccountEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.main.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val accountsRepository: AccountRepository,
) : BaseViewModel() {

    private val _state = MutableLiveData<AuthState>(AuthState.InPending)
    val state = _state.share()

    init {
        getAccount()
    }

    private fun getAccount() = viewModelScope.launch {
        accountsRepository.getAccount().collect { account ->
            account.fold(::onFailure, ::onSuccess)
        }
    }

    private fun onSuccess(account: AccountEntity?) {
        if(account != null) {
            val authenticatedState = AuthState.Authenticated(account)
            _state.value = authenticatedState
        } else {
            val authenticatedState = AuthState.NotAuthenticated
            _state.value = authenticatedState
        }
    }

    private fun onFailure(appFailure: AppFailure) {
        val state = AuthState.NotAuthenticated
        _state.value = state
    }

    fun logOut() = viewModelScope.launch {
        accountsRepository.logout()
    }

}