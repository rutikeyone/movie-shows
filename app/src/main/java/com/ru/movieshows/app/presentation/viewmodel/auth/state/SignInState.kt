package com.ru.movieshows.app.presentation.viewmodel.auth.state

import com.ru.movieshows.sources.accounts.entities.PasswordField
import com.ru.movieshows.sources.accounts.entities.UsernameField

data class SignInState(
    val email: UsernameField = UsernameField(),
    val password: PasswordField = PasswordField(),
    val signInInProgress: Boolean = false,
) {
    val canSignIn = email.isValid && password.isValid && !signInInProgress
}