package com.ru.movieshows.profile.domain

import com.ru.movieshows.profile.domain.repositories.SessionRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {

    suspend fun logout() {
        sessionRepository.clearCurrentSession()
    }

}
