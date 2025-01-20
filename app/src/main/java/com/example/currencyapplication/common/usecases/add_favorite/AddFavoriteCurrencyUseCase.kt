package com.example.currencyapplication.common.usecases.add_favorite

import com.example.currencyapplication.common.model.FavoriteCurrency

interface AddFavoriteCurrencyUseCase {
    operator suspend fun invoke (favoriteCurrency: FavoriteCurrency)
}