package com.ajdev.vnzlaapp.data.repository

import com.ajdev.vnzlaapp.data.model.ExchangeResponse
import com.ajdev.vnzlaapp.data.net.ApiService
import com.ajdev.vnzlaapp.domain.repository.ExchangeRepository
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ExchangeRepository {

    override suspend fun getExchange(): ExchangeResponse {
        return apiService.getExchange()
    }
}