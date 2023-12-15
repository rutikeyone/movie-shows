package com.ru.movieshows.domain.repository

import arrow.core.Either
import com.google.gson.Gson
import com.ru.movieshows.data.dto.AccountDto
import com.ru.movieshows.data.repository.AccountRepository
import com.ru.movieshows.data.repository.AppSettingsRepository
import com.ru.movieshows.domain.entity.AccountEntity
import com.ru.movieshows.domain.entity.AuthenticatedState
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.domain.utils.AsyncLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@Suppress("UNREACHABLE_CODE")
class AccountRepositoryImpl @Inject constructor(
    private val accountDto: AccountDto,
    private val appSettingsRepository: AppSettingsRepository,
    private val gson: Gson,
) : AccountRepository {

    private val currentSessionIdFlow = AsyncLoader {
        MutableStateFlow(appSettingsRepository.getCurrentSessionId())
    }

    private val _state = MutableStateFlow<AuthenticatedState>(AuthenticatedState.Pure)
    override val state: StateFlow<AuthenticatedState> = _state.asStateFlow()

    override fun isSignedIn(): Boolean {
        return state.value is AuthenticatedState.Authenticated
    }

    override suspend fun signIn(email: String, password: String): Either<AppFailure, String> {
        return try {
            val createRequestTokenResult = accountDto.createRequestToken()
            val requestToken = createRequestTokenResult.requestToken
            val createSessionByUsernameAndPassword =
                accountDto.createSessionByUsernameAndPassword(email, password, requestToken)
            val sessionRequestToken = createSessionByUsernameAndPassword.requestToken
            val createSession = accountDto.createSession(sessionRequestToken)
            val sessionId = createSession.sessionId
            currentSessionIdFlow.get().value = sessionId
            appSettingsRepository.setCurrentSessionId(sessionId)
            return Either.Right(sessionId)
        } catch (e: Exception) {
            val error = handleError(e, gson)
            return Either.Left(error)
        }
    }


    override suspend fun logout() {
        appSettingsRepository.cleanCurrentSessionId()
        currentSessionIdFlow.get().value = null
    }


    override suspend fun listenAuthenitationState() {
        currentSessionIdFlow.get().collect { sessionId ->
            val isNotAuthenticated = _state.value is AuthenticatedState.NotAuthenticated
            _state.emit(AuthenticatedState.InPending(isNotAuthenticated))
            try {
                if (sessionId != null) {
                    val result = getAccountBySessionId(sessionId)
                    _state.emit(AuthenticatedState.Authenticated(result))
                } else {
                    _state.emit(AuthenticatedState.NotAuthenticated())
                }
            } catch (e: Exception) {
                val error = handleError(e, gson)
                _state.emit(AuthenticatedState.NotAuthenticated(error))
                appSettingsRepository.cleanCurrentSessionId()
            }
        }
    }

    override suspend fun getAccountBySessionId(sessionId: String): AccountEntity {
        val result = accountDto.getAccountBySessionId(sessionId)
        return result.toEntity()
    }

    companion object {
        const val russianLanguageTag = "ru-RU"
    }

}
