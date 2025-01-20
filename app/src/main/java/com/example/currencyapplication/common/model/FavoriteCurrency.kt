package com.example.currencyapplication.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies_table")
data class FavoriteCurrency(
    @ColumnInfo(name = "first_rate")
    val firstRate: String,
    @ColumnInfo(name = "second_rate")
    val secondRate: String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int? = null
}

