package com.example.currencyapplication.common.usecases.load_favorites

import com.example.currencyapplication.common.repository.CurrenciesFavoriteRepository
import com.example.currencyapplication.common.model.FavoriteCurrency

class LoadFavoriteCurrenciesUseCaseImpl( private val repository: CurrenciesFavoriteRepository):
    LoadFavoriteCurrenciesUseCase {
    override suspend fun loadCurrencies(): List<FavoriteCurrency> = repository.getFavorites()
}