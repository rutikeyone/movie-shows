package com.ru.movieshows.domain.entity

import android.util.Patterns

enum class EmailValidationStatus {
    PURE, EMPTY, INVALID_EMAIL, VALID
}

data class EmailField(
    val value: String = "",
    val status: EmailValidationStatus = EmailValidationStatus.PURE,
) {
    val isValid: Boolean = status == EmailValidationStatus.VALID
    val isPure: Boolean = status == EmailValidationStatus.PURE

    companion object {
        fun validate(value: String?) : EmailValidationStatus {
            val invalid = value?.let { !Patterns.EMAIL_ADDRESS.matcher(it).matches() } ?: true
            if(value.isNullOrEmpty()) return EmailValidationStatus.EMPTY
            else if(invalid) return EmailValidationStatus.INVALID_EMAIL
            return EmailValidationStatus.VALID;
        }
    }
}