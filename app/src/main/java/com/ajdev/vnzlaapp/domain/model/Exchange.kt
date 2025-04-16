package com.ajdev.vnzlaapp.domain.model

import kotlinx.serialization.Serializable

data class Exchange(
    var datetime: Datetime,
    var monitors: Monitors,
)

@Serializable
data class Datetime(
    var date: String,
    var time: String
)

@Serializable
data class Monitors(
    var bcv: ExchangeCompany,
    var enparalelovzla: ExchangeCompany
)

@Serializable
data class ExchangeCompany(
    var image: String,
    var price: Double,
)