package com.ru.movieshows.sources.accounts.entities

import com.ru.movieshows.app.model.AppFailure

sealed class AuthStateEntity {
    object Empty : AuthStateEntity()
    data class Pending(
        val isNotAuthenticated: Boolean
    ) : AuthStateEntity()
    data class Auth(
        val isFirstLaunch: Boolean,
        val account: AccountEntity,
    ) : AuthStateEntity()
    data class NotAuth(
        val isFirstLaunch: Boolean,
        val previousStateAuthenticated: Boolean,
        val error: AppFailure? = null,
    ): AuthStateEntity()
}