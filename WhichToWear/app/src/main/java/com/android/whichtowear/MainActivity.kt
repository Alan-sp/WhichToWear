package com.android.whichtowear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.whichtowear.ui.Datail.DetailScreen
import com.android.whichtowear.ui.theme.WhichToWearTheme
import com.android.whichtowear.ui.Main.MainScreen
import com.android.whichtowear.ui.Main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhichToWearTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen()
{
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main"){
        composable("main"){
            val viewModel = hiltViewModel<MainViewModel>()
            val uiState by viewModel.uiState.observeAsState()
//            Log.d("DEBUG","RUA")
            uiState?.let { it1 ->
                MainScreen(
                    navigate = navController::navigate,
                    updateUiState = viewModel::updateUiState,
                    uiState = it1
                )
            }
        }
        composable(
            "detail/{clothId}",
            arguments = listOf(navArgument("clothId"){
                type = NavType.IntType
            })
        ){
            DetailScreen(
                it.arguments?.getInt("clothId")
            )
        }
    }
}