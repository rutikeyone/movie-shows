package com.ru.movieshows.domain.repository

import arrow.core.Either
import com.google.gson.Gson
import com.ru.movieshows.R
import com.ru.movieshows.data.dto.AccountDto
import com.ru.movieshows.data.repository.AccountRepository
import com.ru.movieshows.data.repository.AppSettingsRepository
import com.ru.movieshows.data.response.CreateSessionWithLoginErrorBody
import com.ru.movieshows.data.response.CreateSessionWithLoginResponse
import com.ru.movieshows.domain.entity.AccountEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.domain.utils.AsyncLoader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
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

    override suspend fun signIn(email: String, password: String) : Either<AppFailure, String> {
        return try {
            val createRequestToken = accountDto.createRequestToken()
            val createRequestBody = createRequestToken.body() ?: return Either.Left(AppFailure.Pure)
            val createRequestTokenValue = createRequestBody.requestToken ?: return Either.Left(AppFailure.Pure)
            val createSessionWithLogin = accountDto.createSessionWithLogin(email, password, createRequestTokenValue)
            val createSessionWithLoginError = getCreateSessionWithLoginError(createSessionWithLogin)
            if(createSessionWithLoginError != null) return createSessionWithLoginError
            val createSessionWithLoginBody = createSessionWithLogin.body() ?: return Either.Left(AppFailure.Pure)
            val createSessionRequestTokenValue = createSessionWithLoginBody.requestToken ?: return Either.Left(AppFailure.Pure)
            val createSession = accountDto.createSession(createSessionRequestTokenValue)
            val createSessionBody = createSession.body() ?: return Either.Left(AppFailure.Pure)
            val sessionId = createSessionBody.sessionId ?: return Either.Left(AppFailure.Pure)
            currentSessionIdFlow.get().value = sessionId
            appSettingsRepository.setCurrentSessionId(sessionId)
            return  Either.Right(sessionId)
        }
        catch (e: Exception) {
            currentSessionIdFlow.get().value = null
            return Either.Left(AppFailure.Pure)
        }
    }

    private fun getCreateSessionWithLoginError(createSessionWithLogin: Response<CreateSessionWithLoginResponse>) : Either<AppFailure, String>? {
        if(createSessionWithLogin.code() == 401) {
            val errorBody = createSessionWithLogin.errorBody() ?: return Either.Left(AppFailure.Pure)
            val charBody = errorBody.charStream() ?: return Either.Left(AppFailure.Pure)
            val createSessionWithLoginErrorBody = Gson().fromJson(charBody.readText(), CreateSessionWithLoginErrorBody::class.java)
            val statusCode = createSessionWithLoginErrorBody.statusCode ?: return Either.Left(AppFailure.Pure)
            if(statusCode == emailNotVerifiedStatusCode || statusCode == invalidPassword) {
                val messageFailure = AppFailure.Message(R.string.invalid_username_or_password)
                return Either.Left(messageFailure)
            }
            return Either.Left(AppFailure.Pure)
        }
        return null
    }

    override suspend fun logout() {
        appSettingsRepository.cleanCurrentSessionId()
        currentSessionIdFlow.get().value = null
    }

    override suspend fun getAccount() : Flow<AccountEntity?> {
        return currentSessionIdFlow.get().flatMapLatest { sessionId ->
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
            appSettingsRepository.cleanCurrentSessionId()
            return null
        }
    }

    companion object {
        const val emailNotVerifiedStatusCode = 32
        const val invalidPassword = 30
    }
}
