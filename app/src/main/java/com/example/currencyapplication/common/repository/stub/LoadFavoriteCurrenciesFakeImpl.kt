package com.example.currencyapplication.common.repository.stub

import com.example.currencyapplication.common.repository.CurrenciesFavoriteRepository
import com.example.currencyapplication.common.model.FavoriteCurrency

class LoadFavoriteCurrenciesFakeImpl: CurrenciesFavoriteRepository {

    override suspend fun getFavorites(): List<FavoriteCurrency> {
        return listOf(FavoriteCurrency("USD","EUR"), FavoriteCurrency("USD","RUB"))
    }

    override suspend fun addFavorite(favoriteCurrency: FavoriteCurrency) {

    }

    override suspend fun deleteFromFavorite(favoriteCurrency: FavoriteCurrency) {

    }
}