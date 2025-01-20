package com.example.currencyapplication.common.usecases.add_favorite

import com.example.currencyapplication.common.repository.CurrenciesFavoriteRepository
import com.example.currencyapplication.common.model.FavoriteCurrency

class AddFavoriteCurrencyUseCaseImpl(private  val favoriteRepository: CurrenciesFavoriteRepository):
    AddFavoriteCurrencyUseCase {
    override suspend fun invoke(favoriteCurrency: FavoriteCurrency) {
        favoriteRepository.addFavorite(favoriteCurrency)
    }
}