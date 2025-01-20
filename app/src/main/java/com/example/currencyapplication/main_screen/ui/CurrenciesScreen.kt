package com.example.currencyapplication.main_screen.ui

import com.example.currencyapplication.common.repository.stub.LoadCurrencyFakeRepository
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.currencyapplication.R
import com.example.currencyapplication.common.repository.stub.LoadFavoriteCurrenciesFakeImpl
import com.example.currencyapplication.common.sorting_preferences.SortingPreferences
import com.example.currencyapplication.common.ui.theme.CurrencyApplicationTheme
import com.example.currencyapplication.common.usecases.add_favorite.AddFavoriteCurrencyUseCaseImpl
import com.example.currencyapplication.common.usecases.delete_currency.DeleteCurrencyUseCaseImpl
import com.example.currencyapplication.common.usecases.load_all_curencies.LoadCurrencyUseCaseImpl
import com.example.currencyapplication.common.usecases.load_favorites.LoadFavoriteCurrenciesUseCaseImpl
import com.example.currencyapplication.common.navigation.Screen
import com.example.currencyapplication.common.ui.CurrencyRow
import com.example.currencyapplication.common.ui.theme.Card
import com.example.currencyapplication.common.ui.theme.Primary
import com.example.currencyapplication.common.ui.theme.TextDefault
import com.example.currencyapplication.common.ui.theme.BackgroundDefault
import com.example.currencyapplication.common.ui.theme.Header
import com.example.currencyapplication.common.ui.theme.Secondary
import com.example.currencyapplication.common.utils.Constants.BASE_CURRENCY

@Composable
fun CurrenciesScreen(navController: NavHostController, viewModel: CurrenciesViewModel = hiltViewModel()) {

    var selectedCurrency by remember { mutableStateOf(value = BASE_CURRENCY) }
    val currencies by viewModel.currency.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = snackbarHostState) {
        viewModel.errors.collect { error ->
            snackbarHostState.showSnackbar(
                message = error.asString(context)
            )
        }
    }

    LaunchedEffect(selectedCurrency) {
        viewModel.load(selectedCurrency)
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
        Column {
            Column (modifier = Modifier
                .background(color = Header)
                .padding(vertical = 16.dp)) {

                Text(
                    stringResource(R.string.currencies),
                    fontSize = 24.sp,
                    color = TextDefault,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CurrencyDropdown(selectedCurrency) {
                        selectedCurrency = it
                    }
                    FilterButton(onClick = { navController.navigate(Screen.Filrers.route) })
                }
            }

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)) {
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(currencies) { currency ->
                        CurrencyRow(currency) { clickedCurrency ->
                            viewModel.toggleFavorite(clickedCurrency, selectedCurrency)
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun CurrencyDropdown(selected: String, onSelectionChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val baseCurrencies = listOf("EUR", "USD", "RUB")

    Box(
        modifier = Modifier
            .width(272.dp)
            .border(1.dp, color = Secondary, RoundedCornerShape(12.dp))
            .background(color = BackgroundDefault, shape = RoundedCornerShape(12.dp))
    ) {
        Button(
            onClick = { expanded = !expanded },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = TextDefault
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(selected, fontSize = 14.sp, color = TextDefault , fontWeight = FontWeight.Medium)

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    modifier = Modifier.size(24.dp),
                    tint = Primary
                )
            }
        }

        if (expanded) {
            Popup(
                alignment = Alignment.TopStart,
                onDismissRequest = { expanded = false }
            ) {
                Box(
                    modifier = Modifier
                        .width(272.dp)
                        .border(1.dp, color = Secondary, RoundedCornerShape(12.dp))
                        .background(color = BackgroundDefault, shape = RoundedCornerShape(12.dp))
                ) {
                    Column {
                        Button(
                            onClick = { expanded = !expanded },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(selected, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                                Icon(
                                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Dropdown",
                                    modifier = Modifier.size(24.dp),
                                    tint = Primary
                                )
                            }
                        }
                        baseCurrencies.forEach { currency ->
                            val isSelected = currency == selected
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        currency,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )
                                },
                                onClick = {
                                    onSelectionChange(currency)
                                    expanded = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color =  if (isSelected) Card else BackgroundDefault)
                                    .padding(6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.background(Color.White),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color = Secondary),
        contentPadding = PaddingValues(12.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.filters_icon),
            contentDescription = "Filter",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CurrenciesPreview() {
    CurrencyApplicationTheme {
        val context = LocalContext.current
        val navController = rememberNavController()
        val fakeRepository = LoadFavoriteCurrenciesFakeImpl()
        val viewModel = CurrenciesViewModel(
            loadCurrencyUseCase = LoadCurrencyUseCaseImpl(repository = LoadCurrencyFakeRepository()),
            sortingPreferences = SortingPreferences(context = context),
            loadFavoriteCurrenciesUseCase = LoadFavoriteCurrenciesUseCaseImpl(repository = fakeRepository),
            addFavoriteCurrencyUseCase = AddFavoriteCurrencyUseCaseImpl(favoriteRepository = fakeRepository),
            deleteCurrencyUseCase = DeleteCurrencyUseCaseImpl(favoriteRepository = fakeRepository)
        )

        CurrenciesScreen(viewModel = viewModel, navController = navController)
    }
}