package com.ru.movieshows.app.presentation.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.app.model.accounts.AccountRepository
import com.ru.movieshows.app.presentation.screens.auth.ProfileFragmentDirections
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.sources.accounts.entities.AuthStateEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class ProfileViewModel @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val accountsRepository: AccountRepository,
) : BaseViewModel() {

    lateinit var authState: LiveData<AuthStateEntity>

    init {
        viewModelScope.launch {
            authState = accountsRepository.getState().asLiveData()
        }
    }

    fun logOut() = viewModelScope.launch {
        accountsRepository.logout()
    }

    fun navigateToSignIn() {
        val action = ProfileFragmentDirections.actionProfileFragmentToSignInFragment2()
        navigator.navigate(action)
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator) : ProfileViewModel
    }

}