package com.meli.challenge.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.codechallenge.model.Success
import com.meli.challenge.api.responses.GetSearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

const val ARG_PRODUCT = "product"

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _viewStateFlow = MutableStateFlow(ProductDetailUiModel.default)
    internal val viewStateFlow get() = _viewStateFlow.asStateFlow()
    private val product: GetSearchResponse.Product = savedStateHandle[ARG_PRODUCT]!!

    init {
        _viewStateFlow.value = _viewStateFlow.value.copy(
            uiStatus = Success,
            product = product,
        )
    }
}
