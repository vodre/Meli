package com.meli.challenge.repository

import com.meli.challenge.api.ApiService
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    SearchRepository
