package com.example.currencyapplication.common.utils

import java.math.BigDecimal
import java.math.RoundingMode

fun Double.displayRate(): String {
    return BigDecimal(this)
        .setScale(6, RoundingMode.HALF_UP)
        .stripTrailingZeros()
        .toPlainString()
}