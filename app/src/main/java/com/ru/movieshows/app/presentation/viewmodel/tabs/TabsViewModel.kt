package com.ru.movieshows.app.presentation.viewmodel.tabs

import androidx.lifecycle.viewModelScope
import com.ru.movieshows.app.model.accounts.AccountRepository
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.sources.accounts.entities.AuthStateEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
) : BaseViewModel() {

    lateinit var authState: Flow<AuthStateEntity>

    init {
        viewModelScope.launch {
            authState = accountRepository.getState()
        }
    }

}