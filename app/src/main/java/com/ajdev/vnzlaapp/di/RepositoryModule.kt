package com.ajdev.vnzlaapp.di

import com.ajdev.vnzlaapp.data.net.ApiService
import com.ajdev.vnzlaapp.data.repository.ExchangeRepositoryImpl
import com.ajdev.vnzlaapp.domain.repository.ExchangeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideExchangeRepository(apiService: ApiService): ExchangeRepository {
        return ExchangeRepositoryImpl(apiService)
    }
}