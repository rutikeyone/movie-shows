package com.ru.movieshows.domain.repository

import arrow.core.Either
import com.google.gson.Gson
import com.ru.movieshows.R
import com.ru.movieshows.data.dto.AccountDto
import com.ru.movieshows.data.repository.AccountRepository
import com.ru.movieshows.data.repository.AppSettingsRepository
import com.ru.movieshows.data.response.AuthenticatedResponse
import com.ru.movieshows.data.response.AuthenticationErrorBody
import com.ru.movieshows.data.response.CreateSessionResponse
import com.ru.movieshows.domain.entity.AccountEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.domain.utils.AsyncLoader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Locale
import javax.inject.Inject


class AccountRepositoryImpl @Inject constructor(
    private val accountDto: AccountDto,
    private val appSettingsRepository: AppSettingsRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val gson: Gson,
) : AccountRepository {

    private val currentSessionIdFlow = AsyncLoader {
        MutableStateFlow(appSettingsRepository.getCurrentSessionId())
    }

    private val _authenticatedFailureFlow = AsyncLoader {
        MutableStateFlow<AppFailure?>(null)
    }

    override suspend fun getAuthenticatedFailureFlow() = _authenticatedFailureFlow.get()

    override suspend fun isSignedIn(): Boolean {
        return appSettingsRepository.getCurrentSessionId() != null
    }

    override suspend fun signIn(email: String, password: String) : Either<AppFailure, String> {
        return try {
            val createRequestTokenResult = createRequestToken()
            val createRequestTokenSuccessResult = createRequestTokenResult.getOrNull()
            val createRequestFailure = createRequestTokenResult.leftOrNull()
            if(createRequestFailure != null || createRequestTokenSuccessResult == null) return Either.Left(createRequestFailure ?: AppFailure.Pure)
            val requestToken = createRequestTokenSuccessResult.requestToken

           val createSessionWithLogin = createSessionWithLogin(email, password, requestToken)
           val createSessionWithLoginSuccessResult = createSessionWithLogin.getOrNull()
           val createSessionWithLoginFailure = createSessionWithLogin.leftOrNull()
           if(createSessionWithLoginFailure != null || createSessionWithLoginSuccessResult == null) return Either.Left(createSessionWithLoginFailure ?: AppFailure.Pure)
           val sessionToken = createSessionWithLoginSuccessResult.requestToken

           val createSessionResult = createSession(sessionToken)
            val createSessionSuccessResult = createSessionResult.getOrNull()
            val createSessionFailure = createSessionResult.leftOrNull()
            if(createSessionFailure != null || createSessionSuccessResult == null) return Either.Left(createSessionFailure ?: AppFailure.Pure)
            val sessionId = createSessionSuccessResult.sessionId

            currentSessionIdFlow.get().value = sessionId
            appSettingsRepository.setCurrentSessionId(sessionId)
            return Either.Right(sessionId)
        }
        catch (e: Exception) {
            currentSessionIdFlow.get().value = null
            return Either.Left(AppFailure.Pure)
        }
    }

    private suspend fun createSession(sessionToken: String) : Either<AppFailure, CreateSessionResponse> {
        return try {
            val createSession = accountDto.createSession(sessionToken)
            val body = createSession.body()
            val errorBody = createSession.errorBody()
            val statusCode = createSession.code()
            if(statusCode == 200 && body != null) {
                return Either.Right(body)
            } else if (errorBody != null) {
                val error = gson.fromJson(errorBody.charStream().readText(), AuthenticationErrorBody::class.java)
                val statusCode = error.statusCode
                if(statusCode == sessionDenied) {
                    return Either.Left(AppFailure.Message(R.string.session_creation_denied))
                }
                return Either.Left(AppFailure.Pure)
            }
            return Either.Left(AppFailure.Pure)
        } catch (e: SocketTimeoutException) {
            return Either.Left(AppFailure.Connection)
        }
        catch (e: UnknownHostException) {
            return Either.Left(AppFailure.Connection)
        }
        catch (e: ConnectException) {
            val tag = Locale.getDefault().toLanguageTag()
            if(tag == russiaLanguageTag) {
                val failure = AppFailure.Message(R.string.problems_may_arise_when_connecting_from_russia_use_a_vpn_service)
                return Either.Left(failure)
            }
            return Either.Left(AppFailure.Connection)
        }
        catch (e: Exception) {
            return Either.Left(AppFailure.Pure)
        }
    }

    private suspend fun createSessionWithLogin(email: String, password: String, requestToken: String) : Either<AppFailure, AuthenticatedResponse> {
        return try {
            val createSessionWithLogin = accountDto.createSessionWithLogin(email, password, requestToken)
            val body = createSessionWithLogin.body()
            val errorBody = createSessionWithLogin.errorBody()
            val statusCode = createSessionWithLogin.code()
            if(statusCode == 200 && body != null) {
                return Either.Right(body)
            } else if(statusCode == 401 && errorBody != null) {
                val error = gson.fromJson(errorBody.charStream().readText(), AuthenticationErrorBody::class.java)
                val statusCode = error.statusCode
                val invalidUsernameOrPassword = statusCode == invalidUsernameOrPassword;
                if(invalidUsernameOrPassword) return Either.Left(AppFailure.Message(R.string.invalid_username_or_password))
                return Either.Left(AppFailure.Pure)
            }
            return Either.Left(AppFailure.Pure)
        } catch (e: SocketTimeoutException) {
            return Either.Left(AppFailure.Connection)
        }
        catch (e: UnknownHostException) {
            return Either.Left(AppFailure.Connection)
        }
        catch (e: ConnectException) {
            val tag = Locale.getDefault().toLanguageTag()
            if(tag == russiaLanguageTag) {
                val failure = AppFailure.Message(R.string.problems_may_arise_when_connecting_from_russia_use_a_vpn_service)
                return Either.Left(failure)
            }
            return Either.Left(AppFailure.Connection)
        }
        catch (e: Exception) {
            return Either.Left(AppFailure.Pure)
        }
    }

    private suspend fun createRequestToken() : Either<AppFailure, AuthenticatedResponse> {
        return try {
            val createRequestToken = accountDto.createRequestToken()
            val body = createRequestToken.body()
            val errorBody = createRequestToken.errorBody()
            if(createRequestToken.code() == 200 && body != null) {
                return Either.Right(body)
            } else if(createRequestToken.code() == 401 && errorBody != null) {
                val error = gson.fromJson(errorBody.charStream().readText(), AuthenticationErrorBody::class.java)
                if(error.statusCode == invalidAPIKey) return Either.Left(AppFailure.Message(R.string.incorrect_api_key))
                return Either.Left(AppFailure.Pure)
            }
            return Either.Left(AppFailure.Pure)
        } catch (e: SocketTimeoutException) {
            return Either.Left(AppFailure.Connection)
        }
        catch (e: UnknownHostException) {
            return Either.Left(AppFailure.Connection)
        }
        catch (e: ConnectException) {
            val tag = Locale.getDefault().toLanguageTag()
            if(tag == russiaLanguageTag) {
                val failure = AppFailure.Message(R.string.problems_may_arise_when_connecting_from_russia_use_a_vpn_service)
                return Either.Left(failure)
            }
            return Either.Left(AppFailure.Connection)
        }
        catch (e: Exception) {
            return Either.Left(AppFailure.Pure)
        }
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
                //_authenticatedFailureFlow.get().value = AppFailure.Pure
                appSettingsRepository.cleanCurrentSessionId()
                return null
            }
        } catch (e: SocketTimeoutException) {
            //_authenticatedFailureFlow.get().value = AppFailure.Connection
            appSettingsRepository.cleanCurrentSessionId()
            return null
        }
        catch (e: UnknownHostException) {
            //_authenticatedFailureFlow.get().value = AppFailure.Connection
            appSettingsRepository.cleanCurrentSessionId()
            return null
        }
        catch (e: ConnectException) {
            val tag = Locale.getDefault().toLanguageTag()
            val failure = if(tag == russiaLanguageTag) {
                AppFailure.Message(R.string.problems_may_arise_when_connecting_from_russia_use_a_vpn_service)
            } else {
                AppFailure.Connection
            }
            //_authenticatedFailureFlow.get().value = failure
            appSettingsRepository.cleanCurrentSessionId()
            return null
        }
        catch (e: Exception) {
            //_authenticatedFailureFlow.get().value = AppFailure.Pure
            appSettingsRepository.cleanCurrentSessionId()
            return null
        }
    }

    companion object {
        const val invalidUsernameOrPassword = 30
        const val invalidAPIKey = 7
        const val sessionDenied = 17
        const val russiaLanguageTag = "ru-RU"
    }
}
