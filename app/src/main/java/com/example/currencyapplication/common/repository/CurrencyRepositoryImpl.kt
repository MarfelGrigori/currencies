package com.example.currencyapplication.common.repository

import com.example.currencyapplication.common.model.Currency
import com.example.currencyapplication.common.network.CurrencyService
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(private var api: CurrencyService):
    CurrencyRepository {

    override suspend fun loadCurrency(symbols: String, base: String): List<Currency> =
        api.loadRates(symbols, base).rates.map { Currency(it.key, it.value) }

}