package com.ru.movieshows.presentation.viewmodel.main

import com.ru.movieshows.domain.entity.AccountEntity

sealed class AuthState {
    object Pure : AuthState()
    object InPending : AuthState()
    data class Authenticated(
        val account: AccountEntity,
    ) : AuthState()
    object NotAuthenticated: AuthState()
}