package com.example.currencyapplication.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.currencyapplication.common.model.FavoriteCurrency


@Dao
interface CurrenciesFavouriteDao {

    @Insert
    suspend fun insert(favoriteCurrency: FavoriteCurrency)

    @Query("SELECT * FROM currencies_table")
    suspend fun getAll(): List<FavoriteCurrency>

    @Query("DELETE FROM currencies_table WHERE first_rate = :firstRate AND second_rate = :secondRate")
    suspend fun delete(firstRate: String, secondRate: String)
}