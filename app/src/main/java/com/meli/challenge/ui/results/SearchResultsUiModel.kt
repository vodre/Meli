package com.meli.challenge.ui.results

import com.example.codechallenge.model.Idle
import com.example.codechallenge.model.UiStatus
import com.meli.challenge.api.responses.GetSearchResponse

internal data class SearchResultsUiModel(
    val uiStatus: UiStatus,
    val keyword: String? = null,
    val results: ArrayList<GetSearchResponse.Product>? = null,
) {
    companion object {
        val default
            get() = SearchResultsUiModel(
                uiStatus = Idle,
            )
    }
}
