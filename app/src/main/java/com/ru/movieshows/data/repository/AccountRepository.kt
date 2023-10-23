package com.ru.movieshows.data.repository

import com.ru.movieshows.domain.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun isSignedIn() : Boolean

    suspend fun signIn(email: String, password: String)

    suspend fun logout()

    suspend fun getAccount() : Flow<AccountEntity?>

    suspend fun getAccountBySessionId(sessionId: String): AccountEntity?
}