package com.ru.movieshows.app.presentation.viewmodel.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.AppGraphDirections
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.model.accounts.AccountRepository
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.sideeffects.resources.Resources
import com.ru.movieshows.app.presentation.sideeffects.toast.Toasts
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.app.presentation.viewmodel.auth.state.SignInState
import com.ru.movieshows.app.utils.share
import com.ru.movieshows.sources.accounts.entities.PasswordField
import com.ru.movieshows.sources.accounts.entities.UsernameField
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class SignInViewModel @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val resources: Resources,
    @Assisted private val toasts: Toasts,
    private val accountRepository: AccountRepository,
) : BaseViewModel() {

    private val _state = MutableLiveData(SignInState())
    val state = _state.share()

    fun changeUsername(value: String) {
        val prevState = _state.value ?: return
        if(prevState.email.isPure && value.isEmpty()) return
        val status = UsernameField.validate(value)
        val email = UsernameField(value, status)
        _state.value = prevState.copy(email = email)
    }

    fun changePassword(value: String) {
        val prevState = _state.value ?: return
        if(prevState.password.isPure && value.isEmpty()) return
        val status = PasswordField.validate(value)
        val password = PasswordField(value, status)
        _state.value = prevState.copy(password = password)
    }

    fun signIn() = viewModelScope.launch {
        val currentState = _state.value ?: return@launch
        val email = _state.value?.email?.value ?: return@launch
        val password = _state.value?.password?.value ?: return@launch
        val canSignIn = _state.value?.canSignIn ?: return@launch
        if(!canSignIn) return@launch
        _state.value = currentState.copy(signInInProgress = true)
        val signInResult = accountRepository.signIn(email, password)
        signInResult.fold(::signInFailureResult, ::signInSuccessResult)
        _state.value = currentState.copy(signInInProgress = false)
    }

    private fun signInFailureResult(failure: AppFailure) {
        val errorResource = failure.errorResource()
        val error = resources.getString(errorResource)
        toasts.toast(error)
    }

    private fun signInSuccessResult(sessionId: String) {}

    fun navigateToTabs() = navigator.targetNavigator {
        it.navigate(AppGraphDirections.actionGlobalTabsFragment())
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator, resources: Resources, toasts: Toasts): SignInViewModel
    }

}