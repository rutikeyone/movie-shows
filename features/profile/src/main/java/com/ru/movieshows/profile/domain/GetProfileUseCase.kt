package com.ru.movieshows.profile.domain

import com.ru.movieshows.core.Container
import com.ru.movieshows.profile.domain.entities.Account
import com.ru.movieshows.profile.domain.repositories.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {

    fun getAccount(): Flow<Container<Account>> {
        return profileRepository.getAccount()
    }

    fun reload() {
        profileRepository.reload()
    }

}