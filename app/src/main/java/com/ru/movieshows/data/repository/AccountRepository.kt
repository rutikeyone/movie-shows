package com.ru.movieshows.data.repository

import arrow.core.Either
import com.ru.movieshows.domain.entity.AccountEntity
import com.ru.movieshows.domain.utils.AppFailure
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    fun isSignedIn() : Boolean

    suspend fun signIn(email: String, password: String) : Either<AppFailure, String>

    suspend fun logout()

    suspend fun getAccount() : Flow<Either<AppFailure, AccountEntity?>>

    suspend fun getAccountBySessionId(sessionId: String):  Either<AppFailure, AccountEntity>
}