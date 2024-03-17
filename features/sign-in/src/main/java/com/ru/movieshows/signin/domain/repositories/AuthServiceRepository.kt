package com.ru.movieshows.signin.domain.repositories

interface AuthServiceRepository {

    suspend fun signIn(email: String, password: String): String

}