package com.example.currencyapplication.common.repository

import com.example.currencyapplication.common.model.FavoriteCurrency

interface CurrenciesFavoriteRepository {

   suspend fun getFavorites(): List<FavoriteCurrency>

   suspend fun addFavorite(favoriteCurrency: FavoriteCurrency)

   suspend fun deleteFromFavorite(favoriteCurrency: FavoriteCurrency)

}