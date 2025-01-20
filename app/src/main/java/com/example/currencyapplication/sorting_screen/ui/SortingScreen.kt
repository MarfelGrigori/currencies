package com.example.currencyapplication.sorting_screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.currencyapplication.R
import com.example.currencyapplication.common.sorting_preferences.SortingOption
import com.example.currencyapplication.common.sorting_preferences.SortingPreferences
import com.example.currencyapplication.common.ui.theme.BackgroundDefault
import com.example.currencyapplication.common.ui.theme.Card
import com.example.currencyapplication.common.ui.theme.CurrencyApplicationTheme
import com.example.currencyapplication.common.ui.theme.Header
import com.example.currencyapplication.common.ui.theme.Primary
import com.example.currencyapplication.common.ui.theme.Secondary
import com.example.currencyapplication.common.ui.theme.TextDefault
import com.example.currencyapplication.common.ui.theme.TextSecondary

@Composable
fun SortingScreen(navController: NavController, viewModel: SortingViewModel = hiltViewModel()) {
    val selectedOption by viewModel.selectedSorting.collectAsState()

    SortingOptions(
        selectedOption = selectedOption,
        onOptionSelected = { viewModel.saveSortingOrder(it) },
        onBackPressed = { navController.navigateUp() }
    )
}

@Composable
fun SortingOptions(
    selectedOption: SortingOption,
    onOptionSelected: (SortingOption) -> Unit,
    onBackPressed: () -> Unit
) {
    var tempSelectedOption by remember { mutableStateOf(selectedOption) }

    LaunchedEffect(selectedOption) {
        tempSelectedOption = selectedOption
    }

    Column(modifier = Modifier.background(color = BackgroundDefault)) {

        FiltersAppBar(onBackPressed)

        Column(modifier = Modifier.padding(16.dp)) {

            Text(text = stringResource(R.string.sort_by), color = TextSecondary, fontSize = 12.sp)

            SortingRow(stringResource(R.string.code_a_z), tempSelectedOption == SortingOption.CodeAZ) {
                tempSelectedOption = SortingOption.CodeAZ
            }
            SortingRow(stringResource(R.string.code_z_a), tempSelectedOption == SortingOption.CodeZA) {
                tempSelectedOption = SortingOption.CodeZA
            }
            SortingRow(stringResource(R.string.quote_asc), tempSelectedOption == SortingOption.QuoteAsc) {
                tempSelectedOption = SortingOption.QuoteAsc
            }
            SortingRow(stringResource(R.string.quote_desc), tempSelectedOption == SortingOption.QuoteDesc) {
                tempSelectedOption = SortingOption.QuoteDesc
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    onOptionSelected(tempSelectedOption)
                    onBackPressed()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text(text = stringResource(R.string.apply))
            }
        }
    }
}

@Composable
fun SortingRow(label: String, isSelected: Boolean, onSelect: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = label, modifier = Modifier.weight(1f), color = TextDefault, fontSize = 16.sp)
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = Primary,
                unselectedColor = Secondary
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersAppBar(onBackPressed: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.filters), color = TextDefault, fontSize = 24.sp)
        },
        colors = topAppBarColors(containerColor = Header),
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = Primary
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun FiltersPreview() {
    CurrencyApplicationTheme {
        val navController = rememberNavController()
        val context = LocalContext.current
        SortingScreen(navController = navController, viewModel = SortingViewModel(sortingPreferences = SortingPreferences(context)))
    }
}