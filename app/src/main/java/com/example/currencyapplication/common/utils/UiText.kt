package com.example.currencyapplication.common.utils

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

@Suppress("unused")
sealed class UiText {

    data class DynamicString(val value: String): UiText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ): UiText()

    class PluralStringResource(
        @PluralsRes val resId: Int,
        val count: Int,
        vararg val args: Any
    ): UiText()

    @Composable
    fun asString(): String {
        return when(this) {
            is DynamicString  -> value
            is StringResource -> stringResource(id = resId, *args)
            is PluralStringResource -> pluralStringResource(id = resId, count, *args)
        }
    }

    fun asString(context: Context): String {
        return when(this) {
            is DynamicString  -> value
            is StringResource -> context.getString(resId, *args)
            is PluralStringResource -> context.resources.getQuantityString(resId, count, *args)
        }
    }

    fun asCommonString(): String {
        return when(this) {
            is DynamicString  -> value
            else -> throw IllegalArgumentException("StringResource should be used only in Composable functions")
        }
    }
}