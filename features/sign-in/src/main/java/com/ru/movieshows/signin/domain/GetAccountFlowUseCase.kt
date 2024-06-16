package com.ru.movieshows.signin.domain

import com.ru.movieshows.core.Container
import com.ru.movieshows.signin.domain.repositories.AuthServiceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccountFlowUseCase @Inject constructor(
    private val authServiceRepository: AuthServiceRepository,
) {

    fun getAccountFlow(): Flow<Container<*>> {
        return authServiceRepository.getAccountFlow()
    }

}