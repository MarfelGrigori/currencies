package com.example.currencyapplication.favorite_screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.currencyapplication.R
import com.example.currencyapplication.common.repository.stub.LoadCurrencyFakeRepository
import com.example.currencyapplication.common.repository.stub.LoadFavoriteCurrenciesFakeImpl
import com.example.currencyapplication.common.sorting_preferences.SortingPreferences
import com.example.currencyapplication.common.ui.CurrencyRow
import com.example.currencyapplication.common.ui.theme.CurrencyApplicationTheme
import com.example.currencyapplication.common.ui.theme.Header
import com.example.currencyapplication.common.ui.theme.TextDefault
import com.example.currencyapplication.common.usecases.delete_currency.DeleteCurrencyUseCaseImpl
import com.example.currencyapplication.common.usecases.load_all_curencies.LoadCurrencyUseCaseImpl
import com.example.currencyapplication.common.usecases.load_favorites.LoadFavoriteCurrenciesUseCaseImpl

@Composable
fun FavoritesScreen(viewModel: FavoriteViewModel = hiltViewModel()) {

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(key1 = snackbarHostState) {
        viewModel.errors.collect{ error ->
            snackbarHostState.showSnackbar(
                message = error.asString(context)
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    val currencies by viewModel.currency.collectAsStateWithLifecycle()

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Header)
            ) {
                Text(stringResource(
                    R.string.favorites),
                    fontSize = 24.sp,
                    color = TextDefault,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
            }
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                items(currencies) { currency ->
                    CurrencyRow(currency) { clickedCurrency ->
                        viewModel.toggleFavorite(clickedCurrency)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val context = LocalContext.current
    CurrencyApplicationTheme {
        val viewModel = FavoriteViewModel(loadCurrencyUseCase = LoadCurrencyUseCaseImpl(repository = LoadCurrencyFakeRepository()),
            loadFavoriteCurrenciesUseCase = LoadFavoriteCurrenciesUseCaseImpl(repository = LoadFavoriteCurrenciesFakeImpl()),
            sortingPreferences = SortingPreferences(context = context),
            deleteCurrencyUseCase = DeleteCurrencyUseCaseImpl(favoriteRepository = LoadFavoriteCurrenciesFakeImpl())
            )
        FavoritesScreen(viewModel = viewModel)
    }
}