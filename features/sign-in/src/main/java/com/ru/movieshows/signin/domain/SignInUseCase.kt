package com.ru.movieshows.signin.domain

import com.ru.movieshows.signin.domain.exceptions.EmptyUsernameException
import com.ru.movieshows.signin.domain.exceptions.EmptyPasswordException
import com.ru.movieshows.signin.domain.repositories.AuthServiceRepository
import com.ru.movieshows.signin.domain.repositories.AuthSessionIdRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authSessionIdRepository: AuthSessionIdRepository,
    private val authServiceRepository: AuthServiceRepository,
) {

    suspend fun signIn(email: String, password: String) {
        if (email.isBlank()) throw EmptyUsernameException()
        if (password.isBlank()) throw EmptyPasswordException()

        val token = authServiceRepository.signIn(email, password)

        authSessionIdRepository.setCurrentSessionId(token)
    }

}