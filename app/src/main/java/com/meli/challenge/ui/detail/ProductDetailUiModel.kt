package com.meli.challenge.ui.detail

import com.example.codechallenge.model.Idle
import com.example.codechallenge.model.UiStatus
import com.meli.challenge.api.responses.GetSearchResponse

internal data class ProductDetailUiModel(
    val uiStatus: UiStatus,
    val product: GetSearchResponse.Product? = null,
) {
    companion object {
        val default
            get() = ProductDetailUiModel(
                uiStatus = Idle,
            )
    }
}
