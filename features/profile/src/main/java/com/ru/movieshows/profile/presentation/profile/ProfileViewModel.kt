package com.ru.movieshows.profile.presentation.profile

import com.ru.movieshows.core.Container
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.profile.domain.GetProfileUseCase
import com.ru.movieshows.profile.domain.IsSignedInUseCase
import com.ru.movieshows.profile.domain.LogoutUseCase
import com.ru.movieshows.profile.presentation.ProfileRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val isSignedInUseCase: IsSignedInUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel() {

    val profileLiveValue = getProfileUseCase.getAccount()
        .toLiveValue(Container.Pending)

    fun reload() = debounce {
        getProfileUseCase.reload()
    }

    fun logout() = debounce {
        viewModelScope.launch {
            val isSigned = isSignedInUseCase.isSignedIn()
            if(!isSigned) return@launch
            logoutUseCase.logout()
        }
    }

}