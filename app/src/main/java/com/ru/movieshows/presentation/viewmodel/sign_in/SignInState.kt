package com.ru.movieshows.presentation.viewmodel.sign_in

import com.ru.movieshows.domain.entity.EmailField
import com.ru.movieshows.domain.entity.PasswordField

data class SignInState(
    val email: EmailField = EmailField(),
    val password: PasswordField = PasswordField(),
    val signInInProgress: Boolean = false,
) {
    val isCanSignIn = email.isValid && password.isValid && !signInInProgress
    val enableView = !signInInProgress
}