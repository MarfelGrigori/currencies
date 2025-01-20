package com.example.currencyapplication.common.model

import com.google.gson.annotations.SerializedName


data class CurrencyResponse(
    @SerializedName("rates")
    val rates: Map<String, Double>
)
