package com.example.currencyapplication.common.usecases.delete_currency

import com.example.currencyapplication.common.model.FavoriteCurrency

interface DeleteCurrencyUseCase {
    operator suspend fun invoke(favoriteCurrency: FavoriteCurrency)
}