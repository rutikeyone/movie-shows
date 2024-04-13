package com.ru.movieshows.profile.domain

import com.ru.movieshows.profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class IsSignedInUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {

    suspend fun isSignedIn(): Boolean {
        return profileRepository.isSignedIn()
    }

}