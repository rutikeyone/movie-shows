package com.ru.movieshows.signin.presentation.signin

import com.ru.movieshows.core.Container
import com.ru.movieshows.core.LoaderOverlay
import com.ru.movieshows.core.NotAuthException
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.PasswordField
import com.ru.movieshows.core.presentation.PasswordValidationStatus
import com.ru.movieshows.core.presentation.UsernameField
import com.ru.movieshows.core.presentation.UsernameValidationStatus
import com.ru.movieshows.signin.R
import com.ru.movieshows.signin.domain.GetAccountFlowUseCase
import com.ru.movieshows.signin.domain.IsSignedInUseCase
import com.ru.movieshows.signin.domain.SignInUseCase
import com.ru.movieshows.signin.domain.exceptions.EmptyPasswordException
import com.ru.movieshows.signin.domain.exceptions.EmptyUsernameException
import com.ru.movieshows.signin.presentation.SignInRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val isSignedInUseCase: IsSignedInUseCase,
    private val signInUseCase: SignInUseCase,
    private val getAccountFlowUseCase: GetAccountFlowUseCase,
    private val loaderOverlay: LoaderOverlay,
    private val router: SignInRouter,
) : BaseViewModel() {

    private val loadScreenStateFlow = MutableStateFlow<Container<Unit>>(Container.Pending)
    private val progressStateFlow = MutableStateFlow(false)
    private val usernameStateFlow = MutableStateFlow(UsernameField())
    private val passwordStateFlow = MutableStateFlow(PasswordField())

    private val canSignIn = combine(
        usernameStateFlow,
        passwordStateFlow,
        progressStateFlow,
        ::mergeCanSignIn,
    )

    val stateLiveValue = combine(
        loadScreenStateFlow,
        progressStateFlow,
        usernameStateFlow,
        passwordStateFlow,
        canSignIn,
        ::mergeState,
    ).toLiveValue(Container.Pending)

    init {
        load()
    }

    private fun load() = debounce {
        viewModelScope.launch {
            loadScreenStateFlow.value = Container.Pending
            try {
                if (isSignedInUseCase.isSignedIn()) {
                    router.launchMain()
                } else {
                    loadScreenStateFlow.value = Container.Success(Unit)
                    collectGetAccountFlow()
                }
            } catch (e: Exception) {
                loadScreenStateFlow.value = Container.Error(e)
            }
        }
    }

    private suspend fun collectGetAccountFlow() {
        getAccountFlowUseCase.getAccountFlow().collect { state ->
            val isProgress = state is Container.Pending
            val isSuccess = state is Container.Success

            if (isProgress) {
                loaderOverlay.showLoader()
            } else {
                loaderOverlay.hideLoader()
            }

            if (isSuccess) {
                router.launchMain()
            }
        }
    }

    fun changeUsernameText(value: String) {
        val oldUsername = usernameStateFlow.value
        if (oldUsername.isPure && value.isEmpty()) return
        val status = UsernameField.validate(value)
        val newUsername = UsernameField(value, status)
        usernameStateFlow.value = newUsername
    }

    fun changePasswordText(value: String) {
        val oldPassword = passwordStateFlow.value
        if (oldPassword.isPure && value.isEmpty()) return
        val newStatus = PasswordField.validate(value)
        val newPassword = PasswordField(value, newStatus)
        passwordStateFlow.value = newPassword
    }

    fun signIn() = debounce {
        viewModelScope.launch {
            try {
                val username = usernameStateFlow.value.value
                val password = passwordStateFlow.value.value
                val canSignIn = canSignIn.firstOrNull() ?: false
                if (!canSignIn) return@launch
                progressStateFlow.value = true
                signInUseCase.signIn(username, password)
            } catch (e: EmptyUsernameException) {
                val oldUsername = usernameStateFlow.value
                usernameStateFlow.value = oldUsername.copy(status = UsernameValidationStatus.EMPTY)
            } catch (e: EmptyPasswordException) {
                val oldPassword = passwordStateFlow.value
                passwordStateFlow.value = oldPassword.copy(status = PasswordValidationStatus.EMPTY)
            } catch (e: NotAuthException) {
                showErrorDialog(resources.getString(R.string.invalid_username_or_password))
            } finally {
                progressStateFlow.value = false
            }
        }
    }

    fun launchMain() = debounce {
        router.launchMain()
    }

    private fun mergeState(
        loadContainer: Container<Unit>,
        inProgress: Boolean,
        usernameField: UsernameField,
        passwordField: PasswordField,
        canSignIn: Boolean,
    ): Container<State> {
        val isTabsNavigationMode = router.isTabsNavigationMode()
        return loadContainer.map {
            State(
                usernameField = usernameField,
                passwordField = passwordField,
                inProgress = inProgress,
                canSignIn = canSignIn,
                isTabsNavigationMode = isTabsNavigationMode,
            )
        }
    }

    private fun mergeCanSignIn(
        usernameField: UsernameField,
        passwordField: PasswordField,
        inProgress: Boolean,
    ): Boolean {
        return usernameField.isValid && passwordField.isValid && !inProgress
    }

    class State(
        val usernameField: UsernameField,
        val passwordField: PasswordField,
        val inProgress: Boolean,
        val canSignIn: Boolean,
        val isTabsNavigationMode: Boolean,
    ) {
        val enableUI: Boolean get() = !inProgress
        val showProgressBar: Boolean get() = inProgress
    }

}