package com.example.currencyapplication.main_screen.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyapplication.R
import com.example.currencyapplication.common.model.Currency
import com.example.currencyapplication.common.model.CurrencyItem
import com.example.currencyapplication.common.model.FavoriteCurrency
import com.example.currencyapplication.common.sorting_preferences.SortingOption
import com.example.currencyapplication.common.sorting_preferences.SortingPreferences
import com.example.currencyapplication.common.usecases.add_favorite.AddFavoriteCurrencyUseCase
import com.example.currencyapplication.common.usecases.delete_currency.DeleteCurrencyUseCase
import com.example.currencyapplication.common.usecases.load_all_curencies.LoadCurrencyUseCase
import com.example.currencyapplication.common.usecases.load_favorites.LoadFavoriteCurrenciesUseCase
import com.example.currencyapplication.common.utils.UiText
import com.example.currencyapplication.common.utils.displayRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val loadCurrencyUseCase: LoadCurrencyUseCase,
    private val loadFavoriteCurrenciesUseCase: LoadFavoriteCurrenciesUseCase,
    private val addFavoriteCurrencyUseCase: AddFavoriteCurrencyUseCase,
    private val deleteCurrencyUseCase: DeleteCurrencyUseCase,
    sortingPreferences: SortingPreferences
) : ViewModel() {

    private val _currency = MutableStateFlow<List<CurrencyItem>>(emptyList())
    val currency: StateFlow<List<CurrencyItem>> = _currency

    private val _selectedSorting  = sortingPreferences.sortingOrder
        .stateIn(viewModelScope, SharingStarted.Eagerly, SortingOption.CodeAZ)

    private val errorChannel = Channel<UiText>()
    val errors = errorChannel.receiveAsFlow()

    fun load(base: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favoriteCurrenciesDeferred = async { loadFavoriteCurrenciesUseCase.loadCurrencies() }
                val allCurrenciesDeferred = async { retry { loadCurrencyUseCase("", base) } }

                val favoriteCurrencies = favoriteCurrenciesDeferred.await()

                val allCurrencies = allCurrenciesDeferred.await()

//                val allCurrencies = listOf(Currency("USD", 0.000006),Currency("AED",16.85),Currency("RUB",122.2)) stub
                val favoriteMap = favoriteCurrencies
                    .filter { it.firstRate == base }
                    .groupBy({ it.firstRate }, { it.secondRate })

                val list = allCurrencies.map { currency ->
                    val isFavorite = favoriteMap[base]?.contains(currency.name) ?: false
                    CurrencyItem(currency.name, currency.rate, isFavorite)
                }

                _currency.value = list
                sortCurrencyList()
            } catch (e: Exception) {
                Log.e("TAG", e.toString())
                errorChannel.send(UiText.StringResource(resId = R.string.unknown_error))
            }
        }
    }

    fun toggleFavorite(currency: CurrencyItem, selectedCurrency: String) {
        _currency.value = _currency.value.map {
            if (it.code == currency.code) it.copy(isFavorite = !it.isFavorite) else it
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favoriteCurrency = FavoriteCurrency(selectedCurrency, currency.code)

                if (isFavorite(favoriteCurrency)) {
                    deleteCurrencyUseCase(favoriteCurrency)
                } else {
                    addFavoriteCurrencyUseCase(favoriteCurrency)
                }
            } catch (e: Exception) {
                errorChannel.send(UiText.StringResource(resId = R.string.unknown_error))
                Log.e("TAG", "DB error ${e.localizedMessage}")
            }
        }
    }

    private suspend fun isFavorite(favoriteCurrency: FavoriteCurrency): Boolean {
        return loadFavoriteCurrenciesUseCase.loadCurrencies().any {
            it.firstRate == favoriteCurrency.firstRate && it.secondRate == favoriteCurrency.secondRate
        }
    }

    private suspend fun retry(
        maxRetries: Int = 3,
        delayMillis: Long = 3000,
        block: suspend () -> List<Currency>
    ): List<Currency> {
        repeat(maxRetries) { attempt ->
            try {
                return block()
            } catch (e: HttpException) {
                Log.e("TAG", "HTTP ${e.code()}, retrying in ${delayMillis / 1000} seconds... Attempt ${attempt + 1}/$maxRetries")
                if (attempt == maxRetries - 1) {
                    val errorMessage =
                        if (e.code() == 429) UiText.StringResource(
                            resId = R.string.network_error_too_many_requests
                        )
                        else UiText.StringResource(resId = R.string.network_error)
                    errorChannel.send(errorMessage)
                }
                delay(delayMillis)
            }
        }
        return emptyList()
    }

    private fun sortCurrencyList() {
        if (_currency.value.isEmpty()) return
        _currency.update { list ->
            when (_selectedSorting.value) {
                SortingOption.CodeAZ -> list.sortedBy { it.code }
                SortingOption.CodeZA -> list.sortedByDescending { it.code }
                SortingOption.QuoteAsc -> list.sortedBy { it.rate }
                SortingOption.QuoteDesc -> list.sortedByDescending { it.rate }
            }
        }
    }

}