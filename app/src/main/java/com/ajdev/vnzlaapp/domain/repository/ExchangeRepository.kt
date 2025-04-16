package com.ajdev.vnzlaapp.domain.repository

import com.ajdev.vnzlaapp.data.model.ExchangeResponse

interface ExchangeRepository {

    suspend fun getExchange(): ExchangeResponse
}