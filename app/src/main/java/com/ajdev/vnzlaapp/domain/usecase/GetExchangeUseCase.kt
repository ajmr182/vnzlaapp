package com.ajdev.vnzlaapp.domain.usecase

import com.ajdev.vnzlaapp.domain.mapper.toDomain
import com.ajdev.vnzlaapp.domain.model.Exchange
import com.ajdev.vnzlaapp.domain.repository.ExchangeRepository
import javax.inject.Inject

class GetExchangeUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {

    suspend operator fun invoke(): Exchange {
        return repository.getExchange().toDomain()
    }
}