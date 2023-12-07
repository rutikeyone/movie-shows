package com.ru.movieshows.presentation.viewmodel.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.AccountRepository
import com.ru.movieshows.domain.entity.AuthenticatedState
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val accountsRepository: AccountRepository,
) : BaseViewModel() {

    val state: LiveData<AuthenticatedState> = accountsRepository.state.asLiveData()

    fun logOut() = viewModelScope.launch {
        accountsRepository.logout()
    }

}