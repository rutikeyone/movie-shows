package com.ru.movieshows.data.repository

import arrow.core.Either
import com.ru.movieshows.domain.entity.AccountEntity
import com.ru.movieshows.domain.entity.AuthenticatedState
import com.ru.movieshows.domain.repository.Repository
import com.ru.movieshows.domain.utils.AppFailure
import kotlinx.coroutines.flow.StateFlow

interface AccountRepository : Repository {

     val state: StateFlow<AuthenticatedState>

    fun isSignedIn() : Boolean

    suspend fun signIn(email: String, password: String) : Either<AppFailure, String>

    suspend fun logout()

    suspend fun getAuthenitationState()

    suspend fun getAccountBySessionId(sessionId: String):  AccountEntity
}