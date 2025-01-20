package com.example.currencyapplication.favorite_screen.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyapplication.R
import com.example.currencyapplication.common.sorting_preferences.SortingOption
import com.example.currencyapplication.common.sorting_preferences.SortingPreferences
import com.example.currencyapplication.common.usecases.delete_currency.DeleteCurrencyUseCase
import com.example.currencyapplication.common.usecases.load_all_curencies.LoadCurrencyUseCase
import com.example.currencyapplication.common.usecases.load_favorites.LoadFavoriteCurrenciesUseCase
import com.example.currencyapplication.common.model.FavoriteCurrency
import com.example.currencyapplication.common.model.CurrencyItem
import com.example.currencyapplication.common.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val loadFavoriteCurrenciesUseCase: LoadFavoriteCurrenciesUseCase,
    private val sortingPreferences: SortingPreferences,
    private val deleteCurrencyUseCase: DeleteCurrencyUseCase,
    private val loadCurrencyUseCase: LoadCurrencyUseCase
): ViewModel() {

    private val errorOccurred = AtomicBoolean(false)

    private val _selectedSorting = sortingPreferences.sortingOrder
        .stateIn(viewModelScope, SharingStarted.Eagerly, SortingOption.CodeAZ)

    private val _currency = MutableStateFlow<List<CurrencyItem>>(emptyList())
    val currency: StateFlow<List<CurrencyItem>> = _currency

    private val errorChannel = Channel<UiText>()
    val errors = errorChannel.receiveAsFlow()

    fun loadFavorites() {
        viewModelScope.launch {

            val list =  try {
                loadFavoriteCurrenciesUseCase.loadCurrencies()
            } catch (e: Exception) {
                handleError(e)
                emptyList()
            }

            val updatedList = list.map { favorite ->
                async(Dispatchers.IO) {
                    try {
                        val rate = loadCurrencyUseCase(favorite.firstRate, favorite.secondRate)
                        CurrencyItem("${favorite.firstRate}/${favorite.secondRate}", rate[0].rate, true)
                    } catch (e: HttpException) {
                        handleError(e)
                        CurrencyItem("${favorite.firstRate}/${favorite.secondRate}", 0.0, true)
                    } catch (e: Exception) {
                        handleError(e)
                        CurrencyItem("${favorite.firstRate}/${favorite.secondRate}", 0.0, true)
                    }
                }
            }.awaitAll()

            _currency.value = updatedList
            sortCurrencyList()
        }
    }

private suspend fun handleError(e: Exception) {
        when (e) {
            is HttpException -> {
                Log.e("TAG", "HTTP error: ${e.code()} - ${e.localizedMessage}")
                val errorMessage =
                    if (e.code() == 429) UiText.StringResource(
                        resId = R.string.network_error_too_many_requests
                    )
                    else UiText.StringResource(resId = R.string.network_error)

                if (errorOccurred.compareAndSet(false, true)) {
                    errorChannel.send(errorMessage)
                }
            }

            else -> {
                Log.e("TAG", "Unknown error: ${e.localizedMessage}")
                if (errorOccurred.compareAndSet(false, true)) {
                    errorChannel.send(UiText.StringResource(resId = R.string.unknown_error))
                }
            }
        }
    }

    fun toggleFavorite(currency: CurrencyItem) {
        _currency.value = _currency.value.map {
            if (it.code == currency.code) it.copy(isFavorite = !it.isFavorite) else it
        }
        val splitedCurrencies = currency.code.split("/")
        viewModelScope.launch(Dispatchers.IO) {
            deleteCurrencyUseCase(FavoriteCurrency(splitedCurrencies[0],splitedCurrencies[1]))
        }
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