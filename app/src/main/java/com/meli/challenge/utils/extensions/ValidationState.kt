package com.meli.challenge.utils.extensions

sealed interface ValidationState {
    val message: String?

    data class Success(override val message: String? = null) : ValidationState
    data class Error(override val message: String? = null) : ValidationState
    data class Changed(override val message: String? = null) : ValidationState
}
