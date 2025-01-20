package com.example.currencyapplication.common.usecases.load_all_curencies

import com.example.currencyapplication.common.repository.CurrencyRepository
import com.example.currencyapplication.common.model.Currency
import javax.inject.Inject

class LoadCurrencyUseCaseImpl @Inject constructor(private val repository: CurrencyRepository):
    LoadCurrencyUseCase {
    override suspend fun invoke(symbols: String, base: String): List<Currency> {
        return repository.loadCurrency(symbols, base)
    }
}