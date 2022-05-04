package com.example.codechallenge.model

sealed interface UiStatus
object Idle : UiStatus
object Loading : UiStatus
object Success : UiStatus
class Failure : UiStatus {
    val error: Throwable?

    constructor(error: Throwable? = null) {
        this.error = error
    }

    constructor(block: () -> Throwable) {
        error = block()
    }
}
