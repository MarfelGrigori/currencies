package com.example.currencyapplication.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyapplication.common.model.FavoriteCurrency

@Database(entities = [FavoriteCurrency::class], version = 1)
abstract class FavoriteCurrenciesDataBase : RoomDatabase() {
    abstract fun currenciesFavoritesDao(): CurrenciesFavouriteDao
}