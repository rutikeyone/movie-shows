package com.ru.movieshows.profile.domain.repositories

import com.ru.movieshows.core.Container
import com.ru.movieshows.profile.domain.entities.Account
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun isSignedIn(): Boolean

    fun getAccount(): Flow<Container<Account>>

    fun reload()

}