package com.example.currencyapplication.common.sorting_preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.currencyapplication.common.utils.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SortingPreferences(private val context: Context) {

    companion object {
        private val SORTING_KEY = intPreferencesKey("sorting_order")
    }

    val sortingOrder: Flow<SortingOption> = context.dataStore.data
        .map { preferences ->
            val value = preferences[SORTING_KEY] ?: SortingOption.CodeAZ.ordinal
            SortingOption.values()[value]
        }

    suspend fun saveSortingOrder(option: SortingOption) {
        context.dataStore.edit { preferences ->
            preferences[SORTING_KEY] = option.ordinal
        }
    }
}