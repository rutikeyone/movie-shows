package com.ru.movieshows.sources.accounts.entities

enum class UsernameValidationStatus {
    PURE, EMPTY, INVALID, VALID,
}

data class UsernameField(
    val value: String = "",
    val status: UsernameValidationStatus = UsernameValidationStatus.PURE
) {
    val isValid: Boolean = status == UsernameValidationStatus.VALID
    val isPure: Boolean = status == UsernameValidationStatus.PURE

    companion object {
        fun validate(value: String?): UsernameValidationStatus {
            if(value.isNullOrEmpty()) return UsernameValidationStatus.EMPTY
            else if(value.length < 3) return UsernameValidationStatus.INVALID
            return UsernameValidationStatus.VALID
        }
    }
}