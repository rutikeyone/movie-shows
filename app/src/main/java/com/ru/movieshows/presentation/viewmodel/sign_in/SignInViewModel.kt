package com.ru.movieshows.presentation.viewmodel.sign_in

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.domain.entity.EmailField
import com.ru.movieshows.domain.entity.PasswordField
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : BaseViewModel() {

    private val _state = MutableLiveData(SignInState())
    val state = _state.share()


    fun changeEmail(value: String) {
        val prevState = _state.value ?: return
        if(prevState.email.isPure && value.isEmpty()) return
        val status = EmailField.validate(value)
        val email = EmailField(value, status)
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

    }

}