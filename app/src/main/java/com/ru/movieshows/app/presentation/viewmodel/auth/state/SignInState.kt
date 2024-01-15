package com.ru.movieshows.app.presentation.viewmodel.auth.state

import com.ru.movieshows.sources.accounts.entities.PasswordFieldEntity
import com.ru.movieshows.sources.accounts.entities.UsernameFieldEntity

data class SignInState(
    val email: UsernameFieldEntity = UsernameFieldEntity(),
    val password: PasswordFieldEntity = PasswordFieldEntity(),
    val signInInProgress: Boolean = false,
) {
    val canSignIn = email.isValid && password.isValid && !signInInProgress
}