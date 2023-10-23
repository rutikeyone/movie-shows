package com.ru.movieshows.domain.repository

import com.ru.movieshows.data.dto.AccountDto
import com.ru.movieshows.data.repository.AccountRepository
import com.ru.movieshows.data.repository.AppSettingsRepository
import com.ru.movieshows.domain.entity.AccountEntity
import com.ru.movieshows.domain.utils.AsyncLoader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDto: AccountDto,
    private val appSettingsRepository: AppSettingsRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : AccountRepository {

    private val currentSessionIdFlow = AsyncLoader {
        MutableStateFlow(appSettingsRepository.getCurrentSessionId())
    }

    override suspend fun isSignedIn(): Boolean {
        return appSettingsRepository.getCurrentSessionId() != null
    }

    override suspend fun signIn(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        appSettingsRepository.cleanCurrentSessionId()
        currentSessionIdFlow.get().value = null
    }

    override suspend fun getAccount() : Flow<AccountEntity?> {
        return currentSessionIdFlow.get().flatMapLatest {sessionId ->
            if(sessionId != null) {
                val result = getAccountBySessionId(sessionId)
                flowOf(result)
            } else {
                flowOf(null)
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun getAccountBySessionId(sessionId: String): AccountEntity? {
        return try {
            val response = accountDto.getAccountBySessionId(sessionId)
            val isSuccess = response.isSuccessful
            val accountModel = response.body()
            if(isSuccess && accountModel != null) {
                return accountModel.toEntity()
            } else {
                return null
            }

        } catch (e: Exception) {
            return null
        }
    }
}
