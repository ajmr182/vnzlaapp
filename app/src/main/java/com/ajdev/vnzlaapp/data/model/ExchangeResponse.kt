package com.ajdev.vnzlaapp.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeResponse(
    @SerializedName("datetime") var datetime: Datetime,
    @SerializedName("monitors") var monitors: Monitors,
)

@Serializable
data class Datetime(
    @SerializedName("date") var date: String,
    @SerializedName("time") var time: String
)

@Serializable
data class Monitors(
    @SerializedName("bcv") var bcv: ExchangeCompany,
    @SerializedName("enparalelovzla") var enparalelovzla: ExchangeCompany
)

@Serializable
data class ExchangeCompany(
    @SerializedName("image") var image: String,
    @SerializedName("price") var price: Double,
)