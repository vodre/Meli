package com.meli.challenge.ui.input

import androidx.lifecycle.ViewModel
import com.example.codechallenge.model.Failure
import com.example.codechallenge.model.Success
import com.meli.challenge.ui.model.UiModel
import com.meli.challenge.utils.TextInput
import com.meli.challenge.utils.validation.SearchInputValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchInputViewModel @Inject constructor() : ViewModel() {

    private val _viewStateFlow = MutableStateFlow(UiModel.default)
    internal val viewStateFlow get() = _viewStateFlow.asStateFlow()
    private val validator = SearchInputValidator()

    init {
    }

    fun validate(textEvent: TextInput.TextEvent) {
        val validationError = validator.validate(textEvent.text)
        _viewStateFlow.value = _viewStateFlow.value.copy(
            uiStatus = if (validationError == null) Success else Failure(),
            keyword = textEvent.text,
            canContinue = validationError == null,
            validatorError = validationError
        )
    }
}
