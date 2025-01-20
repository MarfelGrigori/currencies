package com.example.currencyapplication.common.usecases.delete_currency

import com.example.currencyapplication.common.repository.CurrenciesFavoriteRepository
import com.example.currencyapplication.common.model.FavoriteCurrency

class DeleteCurrencyUseCaseImpl( private val favoriteRepository: CurrenciesFavoriteRepository):
    DeleteCurrencyUseCase {
    override suspend fun invoke(favoriteCurrency: FavoriteCurrency) {
        favoriteRepository.deleteFromFavorite(favoriteCurrency)
    }
}