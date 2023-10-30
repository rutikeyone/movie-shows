package com.ru.movieshows.presentation.viewmodel.sign_in

import com.ru.movieshows.domain.entity.PasswordField
import com.ru.movieshows.domain.entity.UsernameField

data class SignInState(
    val email: UsernameField = UsernameField(),
    val password: PasswordField = PasswordField(),
    val signInInProgress: Boolean = false,
) {
    val canSignIn = email.isValid && password.isValid && !signInInProgress
    val enableView = !signInInProgress
}