package com.ru.movieshows.domain.repository

import arrow.core.Either
import com.google.gson.Gson
import com.ru.movieshows.R
import com.ru.movieshows.data.dto.AccountDto
import com.ru.movieshows.data.repository.AccountRepository
import com.ru.movieshows.data.repository.AppSettingsRepository
import com.ru.movieshows.data.response.AccountErrorModel
import com.ru.movieshows.domain.entity.AccountEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.domain.utils.AsyncLoader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@Suppress("UNREACHABLE_CODE")
class AccountRepositoryImpl @Inject constructor(
    private val accountDto: AccountDto,
    private val appSettingsRepository: AppSettingsRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val gson: Gson,
) : AccountRepository {

    private val currentSessionIdFlow = AsyncLoader {
        MutableStateFlow(appSettingsRepository.getCurrentSessionId())
    }



    override fun isSignedIn(): Boolean {
        return appSettingsRepository.getCurrentSessionId() != null
    }

    override suspend fun signIn(email: String, password: String) : Either<AppFailure, String> {
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
            return handleError(e)
        }
    }
 

    override suspend fun logout() {
        appSettingsRepository.cleanCurrentSessionId()
        currentSessionIdFlow.get().value = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAccount() : Flow<Either<AppFailure, AccountEntity?>> {
        return currentSessionIdFlow.get().flatMapLatest { sessionId ->
            if(sessionId != null) {
                val result = getAccountBySessionId(sessionId)
                flowOf(result)
            } else {
                val emptyResult = Either.Right(null)
                flowOf(emptyResult)
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun getAccountBySessionId(sessionId: String): Either<AppFailure, AccountEntity> {
        return try {
            val result = accountDto.getAccountBySessionId(sessionId)
            val finalResult = result.toEntity()
            return Either.Right(finalResult)
        } catch (e: Exception) {
            appSettingsRepository.cleanCurrentSessionId()
            return handleError(e)
        }
    }

    private fun handleError(e: Exception): Either.Left<AppFailure> {
        return when (e) {
            is retrofit2.HttpException -> {
                val response = e.response()
                val errorBody = response?.errorBody()
                val content = errorBody?.charStream()?.readText()
                if(content != null) {
                    val accountError = gson.fromJson(content, AccountErrorModel::class.java)
                    val accountErrorFailure = AppFailure.fromAccountError(accountError)
                    return Either.Left(accountErrorFailure)
                }
                val pureFailure = AppFailure.Pure
                return Either.Left(pureFailure)
            }
            is SocketTimeoutException -> {
                val connectionFailure = AppFailure.Connection
                return Either.Left(connectionFailure)
            }
            is UnknownHostException -> {
                val connectionFailure = AppFailure.Connection
                return Either.Left(connectionFailure)
            }
            is ConnectException -> {
                if (isRussianTag()) {
                    val messageFailure =
                        AppFailure.Message(R.string.problems_from_russia_connection_message)
                    return Either.Left(messageFailure)
                } else {
                    val connectionFailure = AppFailure.Connection
                    return Either.Left(connectionFailure)
                }
            }
            else -> {
                val pureFailure = AppFailure.Pure
                return Either.Left(pureFailure)
            }
        }
    }


    private fun isRussianTag(): Boolean {
        val tag = Locale.getDefault().toLanguageTag()
        return tag == russianLanguageTag
    }

    companion object {
        const val russianLanguageTag = "ru-RU"
    }
}
