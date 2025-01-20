package com.example.currencyapplication.common.usecases.load_favorites

import com.example.currencyapplication.common.model.FavoriteCurrency

interface LoadFavoriteCurrenciesUseCase {
    suspend fun loadCurrencies(): List<FavoriteCurrency>
}