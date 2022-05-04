package com.meli.challenge.ui.results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codechallenge.model.Failure
import com.example.codechallenge.model.Loading
import com.example.codechallenge.model.Success
import com.meli.challenge.api.ApiService
import com.meli.challenge.api.responses.GetSearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

const val ARG_SEARCH_KEYWORD = "search_keyword"

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val apiService: ApiService,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _viewStateFlow = MutableStateFlow(SearchResultsUiModel.default)
    internal val viewStateFlow get() = _viewStateFlow.asStateFlow()
    private val keyword: String = savedStateHandle[ARG_SEARCH_KEYWORD] ?: "xiaomi"

    init {
        viewModelScope.launch {
            fetchData()
        }
    }

    private fun fetchData() {
        _viewStateFlow.value = _viewStateFlow.value.copy(uiStatus = Loading)
        apiService.getSearch(keyword)
            .enqueue(object : Callback<GetSearchResponse> {
                override fun onResponse(
                    call: Call<GetSearchResponse>,
                    response: Response<GetSearchResponse>
                ) {
                    if (response.isSuccessful) {
                        _viewStateFlow.value = _viewStateFlow.value.copy(
                            uiStatus = Success,
                            keyword = keyword,
                            results = response.body()?.results,
                        )
                    } else {
                        _viewStateFlow.value = _viewStateFlow.value.copy(
                            uiStatus = Failure(Throwable("Network Call Error")),
                        )
                    }
                }

                override fun onFailure(call: Call<GetSearchResponse>, t: Throwable) {
                    _viewStateFlow.value = _viewStateFlow.value.copy(
                        uiStatus = Failure(t),
                    )
                }
            })
    }
}
