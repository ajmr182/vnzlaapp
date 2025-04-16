package com.ajdev.vnzlaapp.domain.mapper

import com.ajdev.vnzlaapp.data.model.ExchangeResponse
import com.ajdev.vnzlaapp.domain.model.Datetime
import com.ajdev.vnzlaapp.domain.model.Exchange
import com.ajdev.vnzlaapp.domain.model.ExchangeCompany
import com.ajdev.vnzlaapp.domain.model.Monitors

fun ExchangeResponse.toDomain(): Exchange {
    return Exchange(
        datetime = Datetime(
            date = this.datetime.date,
            time = this.datetime.time
        ),
        monitors = Monitors(
            bcv = ExchangeCompany(
                image = this.monitors.bcv.image,
                price = this.monitors.bcv.price,
            ),
            enparalelovzla = ExchangeCompany(
                image = this.monitors.enparalelovzla.image,
                price = this.monitors.enparalelovzla.price,
            )
        )
    )
}