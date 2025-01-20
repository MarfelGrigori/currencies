package com.example.currencyapplication.common.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.currencyapplication.favorite_screen.ui.FavoritesScreen
import com.example.currencyapplication.common.ui.BottomNavBar
import com.example.currencyapplication.main_screen.ui.CurrenciesScreen
import com.example.currencyapplication.sorting_screen.ui.SortingScreen

@Composable
fun CurrencyAppNavigation(navController: NavHostController) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != "filters") {
                BottomNavBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                CurrenciesScreen(navController)
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen()
            }
            composable(Screen.Filrers.route) {
                SortingScreen(navController)
            }
        }
    }
}