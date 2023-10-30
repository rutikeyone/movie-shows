package com.ru.movieshows.presentation.viewmodel.profile

import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.AccountRepository
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
) : BaseViewModel() {

    fun logOut() = viewModelScope.launch {
        accountRepository.logout()
    }

}