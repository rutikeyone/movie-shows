package com.ru.movieshows.sources.accounts.entities

import com.ru.movieshows.app.model.AppFailure

sealed class AuthStateEntity {
    object Empty : AuthStateEntity()
    data class Pending(val isNotAuth: Boolean) : AuthStateEntity()
    data class Auth(val isFirstLaunch: Boolean, val account: AccountEntity) : AuthStateEntity()
    data class NotAuth(
        val isFirstLaunch: Boolean,
        val isPrevStateAuth: Boolean,
        val error: AppFailure? = null,
    ): AuthStateEntity()
}