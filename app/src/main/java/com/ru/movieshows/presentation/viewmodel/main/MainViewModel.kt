package com.ru.movieshows.presentation.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.AccountRepository
import com.ru.movieshows.presentation.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val accountsRepository: AccountRepository,
): ViewModel() {
    private val _state = MutableLiveData<AuthState>(AuthState.Pure)
    val state = _state.share()

    init {
        getAccount()
    }

    private fun getAccount() = viewModelScope.launch {
        _state.value = AuthState.InPending
        accountsRepository.getAccount().collect { account ->
            if(account != null) {
                _state.value = AuthState.Authenticated(account)
            } else {
                _state.value = AuthState.NotAuthenticated

            }
        }
    }
}