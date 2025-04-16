package com.ajdev.vnzlaapp.ui.screens.exchangescreen

import androidx.lifecycle.viewModelScope
import com.ajdev.vnzlaapp.domain.model.Exchange
import com.ajdev.vnzlaapp.domain.usecase.GetExchangeUseCase
import com.ajdev.vnzlaapp.ui.BaseViewModel
import com.ajdev.vnzlaapp.ui.utils.formatWithSymbol
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ExchangeViewModel @Inject constructor(
    private val getExchangeUseCase: GetExchangeUseCase
) : BaseViewModel() {

    private val _exchange = MutableStateFlow<Exchange?>(null)
    val exchange: StateFlow<Exchange?> get() = _exchange

    private val _amountToCalculated = MutableStateFlow("0")
    val amountToCalculated: StateFlow<String> get() = _amountToCalculated

    private val _typeOfExchange = MutableStateFlow(ChangeType.DOLLARTOBS)
    val typeOfExchange: StateFlow<ChangeType> get() = _typeOfExchange

    private val _calculatedAmounts = MutableStateFlow(CalculatedAmounts())
    val calculatedAmounts: StateFlow<CalculatedAmounts> get() = _calculatedAmounts

    private val _errorState = MutableStateFlow(false)
    val errorState: StateFlow<Boolean> = _errorState

    init {
      fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(5000)
            try {
                _exchange.value = getExchangeUseCase()
            } catch (e: Exception) {
                _errorState.value = true
                _isLoading.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setExchange(exchangeType: ChangeType) {
        _typeOfExchange.value = exchangeType
    }

    fun setAmount(amount: String) {
        _amountToCalculated.value = amount
    }

    fun calculateAmount() {
        val amount = _amountToCalculated.value.toDoubleOrNull() ?: 0.0
        val bcv = exchange.value?.monitors?.bcv?.price ?: 0.0
        val paralelo = exchange.value?.monitors?.enparalelovzla?.price ?: 0.0
        val promedio = (bcv + paralelo) / 2

        val isDollarToBs = typeOfExchange.value == ChangeType.DOLLARTOBS
        val symbol = if (isDollarToBs) "Bs" else "$"

        val bcvResult = if (isDollarToBs) amount * bcv else amount / bcv
        val parallelResult = if (isDollarToBs) amount * paralelo else amount / paralelo
        val averageResult = if (isDollarToBs) amount * promedio else amount / promedio

        _calculatedAmounts.value = CalculatedAmounts(
            bcv = bcvResult.formatWithSymbol(symbol),
            parallel = parallelResult.formatWithSymbol(symbol),
            average = averageResult.formatWithSymbol(symbol)
        )
    }
}