package com.example.currencyapplication.common.repository

import com.example.currencyapplication.common.db.CurrenciesFavouriteDao
import com.example.currencyapplication.common.model.FavoriteCurrency
import javax.inject.Inject

class CurrenciesFavoriteRepositoryImpl @Inject constructor(
    private val currenciesDao: CurrenciesFavouriteDao
) : CurrenciesFavoriteRepository {

    override suspend fun getFavorites(): List<FavoriteCurrency> = currenciesDao.getAll()

    override suspend fun addFavorite(favoriteCurrency: FavoriteCurrency) =
        currenciesDao.insert(favoriteCurrency)

    override suspend fun deleteFromFavorite(favoriteCurrency: FavoriteCurrency) =
        currenciesDao.delete(favoriteCurrency.firstRate, favoriteCurrency.secondRate)
}