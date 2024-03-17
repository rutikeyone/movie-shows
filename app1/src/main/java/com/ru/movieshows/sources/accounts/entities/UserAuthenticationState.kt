package com.ru.movieshows.sources.accounts.entities

import com.ru.movieshows.app.model.AppFailure

sealed class UserAuthenticationState {

    object Empty : UserAuthenticationState()

    data class Pending(
        val previousNotAuthenticated: Boolean
    ) : UserAuthenticationState()

    data class Authentication(
        val firstLaunch: Boolean,
        val account: AccountEntity
    ) : UserAuthenticationState()

    data class NotAuthentication(
        val firstLaunch: Boolean,
        val previousAuthenticated: Boolean,
        val error: AppFailure? = null
    ): UserAuthenticationState()

}
