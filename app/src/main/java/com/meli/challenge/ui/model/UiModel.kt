package com.meli.challenge.ui.model

import com.example.codechallenge.model.Idle
import com.example.codechallenge.model.UiStatus
import com.meli.challenge.utils.validation.SearchInputValidationError

internal data class UiModel(
    val uiStatus: UiStatus,
    val keyword: String = "",
    val canContinue: Boolean = false,
    val validatorError: SearchInputValidationError? = null
) {
    companion object {
        val default
            get() = UiModel(
                uiStatus = Idle,
            )
    }
}
