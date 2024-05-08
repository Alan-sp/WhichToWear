package com.android.whichtowear.ui.Main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.whichtowear.ui.Closet.ClosetScreen
import com.android.whichtowear.ui.Closet.ClosetViewModel
import com.android.whichtowear.ui.Main.nav.MainBottomNav
import com.android.whichtowear.ui.Suit.SuitScreen

@Composable
fun MainScreen(
    navigate:(String) -> Unit,
    updateUiState: (MainUiState) -> Unit,
    uiState:MainUiState
)
{
    val navController:NavHostController = rememberNavController()
    var navBarVisible by remember { mutableStateOf(true) }

    Scaffold (
        bottomBar = {
            AnimatedVisibility(
                visible = navBarVisible,
                enter = slideInVertically(
                    animationSpec = tween(
                        durationMillis = 100
                    )
                ) { it },
                exit = fadeOut()
            ){
                MainBottomNav(navController,uiState,updateUiState)
            }
        }
    ){innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = "closet"
        ){
            composable("closet")
            {
                val viewModel = hiltViewModel<ClosetViewModel>()
                val closetUiState by viewModel.uiState.collectAsState()
                ClosetScreen(
                    uiState = closetUiState,
                    addPhotos = viewModel::addPhotos,
                    navigate = navigate
                )
            }
            composable("suit")
            {
                SuitScreen()
            }
            composable("outfit")
            {

            }
        }
    }

}