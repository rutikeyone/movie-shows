package com.ru.movieshows.app.glue.signin.repositories

import com.ru.movieshows.data.AccountsDataRepository
import com.ru.movieshows.signin.domain.repositories.AuthServiceRepository
import javax.inject.Inject

class AdapterAuthServiceRepository @Inject constructor(
    private val accountsDataRepository: AccountsDataRepository,
) : AuthServiceRepository {

    override suspend fun signIn(email: String, password: String): String {
        return accountsDataRepository.signIn(email, password)
    }

}