package com.ru.movieshows.presentation.viewmodel.sign_in

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.AccountRepository
import com.ru.movieshows.domain.entity.PasswordField
import com.ru.movieshows.domain.entity.UsernameField
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.utils.ToastIntent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
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
        val toastIntent = ToastIntent(failure.errorResource())
        toastEvent.publishEvent(toastIntent)
    }

    private fun signInSuccessResult(sessionId: String) {}
}