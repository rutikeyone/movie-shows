package com.ru.movieshows.domain.entity

import com.ru.movieshows.domain.utils.AppFailure

sealed class AuthenticatedState {
    object Pure : AuthenticatedState()
    data class InPending(
        val isNotAuthenticated: Boolean
    ) : AuthenticatedState()
    data class Authenticated(
        val account: AccountEntity,
    ) : AuthenticatedState()
    data class NotAuthenticated(
        val error: AppFailure? = null,
    ): AuthenticatedState()
}