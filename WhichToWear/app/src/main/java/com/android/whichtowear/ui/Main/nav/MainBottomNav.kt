package com.android.whichtowear.ui.Main.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.android.whichtowear.ui.Main.MainUiState

@Composable
fun MainBottomNav(navController: NavController,uiState:MainUiState,updateUiState: (MainUiState) -> Unit)
{
    NavigationBar {
        NavigationBarItem(selected = uiState.route == MainUiState.Closet.route,
            onClick = {
                updateUiState(MainUiState.Closet)
                navController.navigate(MainUiState.Closet.route)
              },
            icon = { Icon(Icons.Default.Home, contentDescription = "") },
            label = { Text(text = MainUiState.Closet.route) },
        )

        NavigationBarItem(selected = uiState.route == MainUiState.Suit.route,
            onClick = {
                updateUiState(MainUiState.Suit)
                navController.navigate(MainUiState.Suit.route)
              },
            icon = { Icon(Icons.Default.Notifications, contentDescription = "") },
            label = { Text(text = MainUiState.Suit.route) })

        NavigationBarItem(selected = uiState.route == MainUiState.Outfit.route,
            onClick = {
                updateUiState(MainUiState.Outfit)
                navController.navigate(MainUiState.Outfit.route)
            },
            icon = { Icon(Icons.Default.Menu, contentDescription = "") },
            label = { Text(text = MainUiState.Outfit.route) })
    }
}