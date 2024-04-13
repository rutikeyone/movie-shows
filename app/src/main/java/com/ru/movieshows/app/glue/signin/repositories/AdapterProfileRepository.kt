package com.ru.movieshows.app.glue.signin.repositories

import com.ru.movieshows.core.NotAuthException
import com.ru.movieshows.core.unwrapFirst
import com.ru.movieshows.data.AccountsDataRepository
import com.ru.movieshows.signin.domain.repositories.ProfileRepository
import javax.inject.Inject

class AdapterProfileRepository @Inject constructor(
    private val accountsDataRepository: AccountsDataRepository,
) : ProfileRepository {

    override suspend fun canLoadProfile(): Boolean {
        return try {
            accountsDataRepository.getAccount().unwrapFirst()
            true
        } catch (ignored: NotAuthException) {
            false
        }
    }
}