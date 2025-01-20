package com.example.currencyapplication.common.usecases.load_all_curencies

import com.example.currencyapplication.common.model.Currency
import com.example.currencyapplication.common.model.CurrencyResponse

interface LoadCurrencyUseCase {
    suspend operator fun invoke(symbols: String, base: String): List<Currency>
}