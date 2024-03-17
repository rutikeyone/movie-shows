package com.ru.movieshows.signin.domain.repositories

interface ProfileRepository {

    suspend fun canLoadProfile(): Boolean

}