package com.meli.challenge.utils.validation

const val MAX_CHILD_NAME_LENGTH = 20 // Sample Size

class SearchInputValidator constructor() {
    fun validate(value: String): SearchInputValidationError? = when {
        value.isEmpty() -> SearchInputValidationError.EMPTY_NAME
        value.length > MAX_CHILD_NAME_LENGTH -> SearchInputValidationError.LENGTH_EXCEEDED
        else -> null
    }
}

enum class SearchInputValidationError {
    EMPTY_NAME,
    LENGTH_EXCEEDED,
}
