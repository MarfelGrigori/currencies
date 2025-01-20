package com.example.currencyapplication.common.repository.stub

import com.example.currencyapplication.common.repository.CurrencyRepository
import com.example.currencyapplication.common.model.Currency

class LoadCurrencyFakeRepository : CurrencyRepository {
    override suspend fun loadCurrency(symbols: String, base: String): List<Currency> {
        return listOf(
            Currency("USD", 1.1),
            Currency("EUR", 0.9),
            Currency("GBP", 0.8)
        )
    }
}