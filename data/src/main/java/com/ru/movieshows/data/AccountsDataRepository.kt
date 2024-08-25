package com.ru.movieshows.data

import com.ru.movieshows.core.Container
import com.ru.movieshows.data.accounts.models.AccountDataModel
import kotlinx.coroutines.flow.Flow

interface AccountsDataRepository {

    fun getAccount(): Flow<Container<AccountDataModel>>

    suspend fun isSignedIn(): Boolean

    suspend fun signIn(username: String, password: String): String

    fun logout()

    fun reload()

}