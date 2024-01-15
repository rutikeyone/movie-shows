package com.ru.movieshows.sources.accounts.entities

enum class PasswordValidationStatus {
    PURE, EMPTY, INVALID, VALID
}

data class PasswordFieldEntity(
    val value: String = "",
    val status: PasswordValidationStatus = PasswordValidationStatus.PURE,
) {
    val isValid: Boolean = status == PasswordValidationStatus.VALID
    val isPure: Boolean = status == PasswordValidationStatus.PURE

    companion object {
        fun validate(value: String?): PasswordValidationStatus {
            if(value.isNullOrEmpty()) return PasswordValidationStatus.EMPTY
            else if(value.length < 3) return PasswordValidationStatus.INVALID
            return PasswordValidationStatus.VALID
        }
    }
}