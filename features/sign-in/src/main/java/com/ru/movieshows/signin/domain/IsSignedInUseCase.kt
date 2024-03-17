package com.ru.movieshows.signin.domain

import com.ru.movieshows.signin.domain.repositories.AuthSessionIdRepository
import com.ru.movieshows.signin.domain.repositories.ProfileRepository
import javax.inject.Inject

class IsSignedInUseCase @Inject constructor(
    private val authSessionIdRepository: AuthSessionIdRepository,
    private val profileRepository: ProfileRepository,
) {

    suspend fun isSignedIn(): Boolean {
        return authSessionIdRepository.getCurrentSessionId() != null
                && profileRepository.canLoadProfile()
    }

}