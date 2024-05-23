package com.android.whichtowear.ui.Main.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.android.whichtowear.R
import com.android.whichtowear.ui.Main.MainUiState

@Composable
fun MainBottomNav(navController: NavController, uiState: MainUiState, updateUiState: (MainUiState) -> Unit)
{
    NavigationBar {
        NavigationBarItem(selected = uiState.route == MainUiState.Closet.route,
            onClick = {
                updateUiState(MainUiState.Closet)
                navController.navigate(MainUiState.Closet.route)
              },
            label = { Text(text = "衣橱") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.closet),
                    contentDescription = ""
                )
            },
        )

        NavigationBarItem(selected = uiState.route == MainUiState.Suit.route,
            onClick = {
                updateUiState(MainUiState.Suit)
                navController.navigate(MainUiState.Suit.route)
              },
            label = { Text(text = "穿搭") },
            icon = { Icon(Icons.Default.AddTask, contentDescription = "")},
        )
        NavigationBarItem(selected = uiState.route == MainUiState.Wearing.route,
            onClick = {
                updateUiState(MainUiState.Wearing)
                navController.navigate(MainUiState.Wearing.route)
            },
            icon = { Icon(Icons.Default.Menu, contentDescription = "") },
            label = { Text(text = "记录") })
    }
}