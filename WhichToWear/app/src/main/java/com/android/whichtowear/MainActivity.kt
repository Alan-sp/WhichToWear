package com.android.whichtowear

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
//import com.android.whichtowear.survey.SurveyResultScreen
import com.android.whichtowear.ui.survey.SurveyRoute
import com.android.whichtowear.ui.Detail.DetailScreen
import com.android.whichtowear.ui.Detail.DetailViewModel
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
    val context = LocalContext.current
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Request permissions if not granted
        ActivityCompat.requestPermissions(
            context as ComponentActivity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            1
        )
    }

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "main"
    ){
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

        composable("survey"){
            SurveyRoute(
                onSurveyComplete = {
                    navController.navigate("main")
                },
                onNavUp = navController::navigateUp,
            )
        }

        composable(
            "detail/{clothId}",
            arguments = listOf(navArgument("clothId"){ type = NavType.IntType })
        ){
            val viewModel = hiltViewModel<DetailViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            DetailScreen(
                uiState,
                viewModel::addToWearing,
                viewModel::delete,
                navController::popBackStack
            )
        }
    }
}
