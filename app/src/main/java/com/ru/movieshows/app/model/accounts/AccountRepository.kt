package com.ru.movieshows.app.model.accounts

import arrow.core.Either
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.sources.accounts.entities.AccountEntity
import com.ru.movieshows.sources.accounts.entities.UserAuthenticationState
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun getUserAuthenticationState(): Flow<UserAuthenticationState>

    suspend fun isSignedIn(): Boolean

    suspend fun signIn(email: String, password: String): Either<AppFailure, String>

    suspend fun logout()

    suspend fun getAccountBySessionId(sessionId: String): AccountEntity

}