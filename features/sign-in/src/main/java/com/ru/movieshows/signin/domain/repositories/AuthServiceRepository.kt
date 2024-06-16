package com.ru.movieshows.signin.domain.repositories

import com.ru.movieshows.core.Container
import kotlinx.coroutines.flow.Flow

interface AuthServiceRepository {

    suspend fun signIn(email: String, password: String): String

    fun getAccountFlow(): Flow<Container<*>>

}