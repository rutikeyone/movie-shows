package com.ru.movieshows.data.repository

import arrow.core.Either
import com.ru.movieshows.domain.entity.AccountEntity
import com.ru.movieshows.domain.utils.AppFailure
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun isSignedIn() : Boolean

    suspend fun signIn(email: String, password: String) : Either<AppFailure, String>

    suspend fun logout()

    suspend fun getAccount() : Flow<AccountEntity?>

    suspend fun getAccountBySessionId(sessionId: String): AccountEntity?
}