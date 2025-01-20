package com.example.currencyapplication.common.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorites : Screen("favorites")
    object Filrers : Screen("filters")
}