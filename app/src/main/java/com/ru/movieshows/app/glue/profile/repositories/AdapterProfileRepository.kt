package com.ru.movieshows.app.glue.profile.repositories

import com.ru.movieshows.app.formatters.ImageUrlFormatter
import com.ru.movieshows.core.Container
import com.ru.movieshows.data.AccountsDataRepository
import com.ru.movieshows.profile.domain.entities.Account
import com.ru.movieshows.profile.domain.repositories.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AdapterProfileRepository @Inject constructor(
    private val accountsDataRepository: AccountsDataRepository,
    private val imageUrlFormatter: ImageUrlFormatter,
) : ProfileRepository {

    override fun getAccount(): Flow<Container<Account>> {
        return accountsDataRepository.getAccount().map { container ->
            container.map {
                val avatar = it.avatar?.tmdb?.avatarPath

                Account(
                    id = it.id,
                    name = it.name,
                    includeAdult = it.includeAdult,
                    username = it.username,
                    avatarPath = if (!avatar.isNullOrEmpty()) imageUrlFormatter.toImageUrl(avatar) else null
                )
            }
        }
    }

    override suspend fun isSignedIn(): Boolean {
        return accountsDataRepository.isSignedIn()
    }

    override fun reload() {
        accountsDataRepository.reload()
    }

}