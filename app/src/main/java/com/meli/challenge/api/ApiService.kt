package com.meli.challenge.api

import com.meli.challenge.api.responses.GetSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("sites/MLA/search")
    fun getSearch(@Query("q") q: String?):
        Call<GetSearchResponse>
}
