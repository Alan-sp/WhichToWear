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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.whichtowear.db.entity.Clothing
import com.android.whichtowear.ui.Closet.ClosetScreen
import com.android.whichtowear.ui.Closet.ClosetUiState
import com.android.whichtowear.ui.Closet.ClosetViewModel
import com.android.whichtowear.ui.Main.nav.MainBottomNav
import com.android.whichtowear.ui.Suit.SuitScreen
import com.android.whichtowear.ui.Suit.SuitViewModel

@Composable
fun MainScreen(
    navigate:(String) -> Unit,
    updateUiState: (MainUiState) -> Unit,
    uiState: MainUiState
)
{
    val navController:NavHostController = rememberNavController()
    var navBarVisible by remember { mutableStateOf(true) }
    val closetViewModel = hiltViewModel<ClosetViewModel>()
    val viewModel = hiltViewModel<SuitViewModel>()

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
                val tabState by closetViewModel.TabUiState.observeAsState()
                val nowState by closetViewModel.uiState.collectAsState()
                val closetUiState = getUiStates(nowState,tabState?:0)
                tabState?.let { it1 ->
                    ClosetScreen(
                        uiState = closetUiState,
                        TabUiState = it1,
                        changeTabUiState = closetViewModel::changeTabUiState,
                        navigate = navigate
                    )
                }
            }
            composable("suit")
            {
                val uiState by viewModel.uiState.collectAsState()
                val weatherState by viewModel.weatherState.collectAsState()
                val allState by viewModel.allState.collectAsState()
                SuitScreen(
                    uiState = uiState,
                    allState = allState,
                    weatherState = weatherState,
                    changeWeatherState = viewModel::changeWeatherState,
                    navigate = navigate,
                    updatePhotos = viewModel::updatePhotos,
                )
            }
            composable("outfit")
            {

            }
        }
    }
}
fun getUiStates(
    uiState: ClosetUiState,
    tabUiState: Int
):ClosetUiState
{
    if(uiState !is ClosetUiState.PhotoList)
        return ClosetUiState.PhotoList(emptyList())
    val resList = mutableListOf<Clothing>()
    for(i in uiState.photos.indices)
    {
        if(uiState.photos[i].type == tabUiState)
            resList.add(uiState.photos[i])
    }
    return ClosetUiState.PhotoList(resList)
}