package com.example.currencyapplication.common.repository

import com.example.currencyapplication.common.model.Currency
import com.example.currencyapplication.common.model.CurrencyResponse

interface CurrencyRepository {
    suspend fun loadCurrency(symbols: String, base: String): List<Currency>
}