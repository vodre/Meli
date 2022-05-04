package com.meli.challenge.di

import com.meli.challenge.repository.SearchRepository
import com.meli.challenge.repository.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsSearchRepository(challengeRepositoryImpl: SearchRepositoryImpl):
        SearchRepository
}
