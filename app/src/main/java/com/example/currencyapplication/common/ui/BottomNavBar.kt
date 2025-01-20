package com.example.currencyapplication.common.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.currencyapplication.R
import com.example.currencyapplication.common.navigation.Screen
import com.example.currencyapplication.common.navigation.BottomNavItem
import com.example.currencyapplication.common.ui.theme.BackgroundDefault
import com.example.currencyapplication.common.ui.theme.Primary
import com.example.currencyapplication.common.ui.theme.Secondary

@Composable
fun BottomNavBar(navController: NavHostController) {
    val bottomNavItems = listOf(
        BottomNavItem(stringResource(id = R.string.currencies), R.drawable.wallet_icon, Screen.Home.route),
        BottomNavItem(stringResource(id = R.string.favorites), R.drawable.favorites_icon_bottom_nav_view, Screen.Favorites.route)
    )

    NavigationBar(containerColor = BackgroundDefault) {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route

        bottomNavItems.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        tint = setBottomNavItemColors(isSelected)
                    )
                },
                label = { Text(item.label, color = setBottomNavItemColors(isSelected)) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun setBottomNavItemColors(isSelected: Boolean) =
    if (isSelected) Primary else Secondary