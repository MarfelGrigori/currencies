package com.example.currencyapplication.common.network

import com.example.currencyapplication.common.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    @GET("latest")
    suspend fun loadRates(
        @Query("symbols") symbols: String,
        @Query("base") base: String
    ): CurrencyResponse
}