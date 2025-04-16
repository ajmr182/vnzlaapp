package com.ajdev.vnzlaapp.data.net

import com.ajdev.vnzlaapp.data.model.ExchangeResponse
import retrofit2.http.GET

interface ApiService {

    @GET("dollar")
    suspend fun getExchange(): ExchangeResponse
}