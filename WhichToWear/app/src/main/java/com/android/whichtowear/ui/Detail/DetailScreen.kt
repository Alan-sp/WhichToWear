package com.android.whichtowear.ui.Detail

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.whichtowear.R
import com.android.whichtowear.db.entity.Clothing
import com.android.whichtowear.getDateFromTimeStamp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: DetailUiState,
    //addToOutfit: (clothing: Clothing, () -> Unit) -> Unit,
    //addToLaundry: (() -> Unit) -> Unit,
    delete: (() -> Unit) -> Unit,
    popBackStack: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detail") },
                navigationIcon = {
                    IconButton(onClick = { popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    FilledTonalIconButton(onClick = {
                        showBottomSheet = true
                    }) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete")
                    }
                }
            )
        },
        bottomBar = {
            if (uiState is DetailUiState.OpenDetail)
                DetailScreenBottomAppBar(context, uiState.clothing,)//addToOutfit, addToLaundry)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (uiState is DetailUiState.OpenDetail) {
                val clothing = uiState.clothing
                Column(modifier = Modifier.fillMaxSize()) {
                    DetailScreenImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f / 1.5f)
                            .align(Alignment.CenterHorizontally),
                        image = clothing.image
                    )
                    Box(modifier = Modifier.height(16.dp))
//
//                    if (uiState.laundryCount != null && uiState.laundryCount > 0) {
//                        Box(modifier = Modifier.height(16.dp))
//                        DetailScreenLaundry(
//                            modifier = Modifier.fillMaxWidth(),
//                            laundryCount = uiState.laundryCount,
//                            lastLaundryDate = uiState.lastLaundryDate
//                        )
//                    }
                }
            } else {
                Text(text = "Detail Error")
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                dragHandle = {}
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "Delete? It will be removed from outfits too.")
                    Box(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                            delete {
                                popBackStack()
                                Toast.makeText(context, "Clothing Deleted", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete")
                        Box(modifier = Modifier.width(16.dp))
                        Text(text = "Delete", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailScreenBottomAppBar(
    context: Context,
    clothing: Clothing,
    //addToOutfit: (clothing: Clothing, () -> Unit) -> Unit,
    //addToLaundry: (() -> Unit) -> Unit
) {
    BottomAppBar(
        actions = {
//            FilledTonalButton(onClick = {
//                addToOutfit(clothing) {
//                    Toast.makeText(context, "Added to outfit", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }) {
//                Icon(
//                    painter = painterResource(id = R.drawable.closet),
//                    contentDescription = "Add to outfit"
//                )
//                Box(modifier = Modifier.width(4.dp))
//                Text(text = "Add to outfit")
//            }
//            Box(modifier = Modifier.width(16.dp))
//            FilledTonalButton(onClick = {
//                addToLaundry() {
//                    Toast.makeText(context, "Added to laundry", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.laundry),
//                    contentDescription = "Add to laundry"
//                )
//                Box(modifier = Modifier.width(4.dp))
//                Text(text = "Add to laundry")
//            }
        },

//        floatingActionButton = {
//            ExtendedFloatingActionButton(
//                expanded = false,
//                text = { Text(text = "Edit") },
//                icon = {
//                    Icon(
//                        imageVector = Icons.Outlined.Edit,
//                        contentDescription = "Edit"
//                    )
//                },
//                onClick = {}
//            )
//        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun DetailScreenImage(modifier: Modifier = Modifier, image: String) {
    if (image.isNotEmpty())
        GlideImage(
            modifier = modifier.clip(shape = RoundedCornerShape(4.dp)),
            model = image,
            contentScale = ContentScale.Crop,
            contentDescription = "Clothing Image"
        )
    else
        Box(
            modifier = modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .background(color = Color.Gray)
        )
}

//@Composable
//private fun DetailScreenLaundry(modifier: Modifier, laundryCount: Int, lastLaundryDate: Long?) {
//    val mLastLaundryDate =
//        if (lastLaundryDate != null) getDateFromTimeStamp(lastLaundryDate) else ""
//    Card(modifier = modifier, shape = RoundedCornerShape(4.dp)) {
//        Row(modifier = Modifier.padding(16.dp)) {
//            Column(
//                modifier = Modifier.weight(1f),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = laundryCount.toString(),
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp
//                )
//                Text(text = "Total Laundry", fontSize = 12.sp)
//            }
//            Column(
//                modifier = Modifier.weight(1f),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = mLastLaundryDate,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp
//                )
//                Text(text = "Last Laundry", fontSize = 12.sp)
//            }
//        }
//    }
//}