package com.android.whichtowear.ui.Closet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClosetScreen()
{
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Closet") })
        },
        floatingActionButton = {
//            if (uiState is ClosetUiState.PhotoList && uiState.photos.isNotEmpty())
                ExtendedFloatingActionButton(
                    onClick = {
//                        launcher.launch(
//                            PickVisualMediaRequest(
//                                ActivityResultContracts.PickVisualMedia.ImageOnly
//                            )
//                        )
                    },
                    text = { Text("Clothes") },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add"
                        )
                    }
                )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
//            if (uiState is ClosetUiState.PhotoList) {
//                if (uiState.photos.isNotEmpty()) {
//                    Log.d("number", "${uiState.photos.size}")
//                    LazyVerticalGrid(
//                        columns = GridCells.Adaptive(minSize = 80.dp),
//                        verticalArrangement = Arrangement.spacedBy(4.dp),
//                        horizontalArrangement = Arrangement.spacedBy(4.dp),
//                        content = {
//                            itemsIndexed(uiState.photos) { index, photo ->
//                                GlideImage(
//                                    model = photo.image,
//                                    contentScale = ContentScale.Crop,
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .aspectRatio(1f / 1.6f)
//                                        .clip(shape = RoundedCornerShape(4.dp))
//                                        .clickable {
//                                            navigate("detail/${photo.id}")
//                                        }
//                                )
//                            }
//                        },
//                        modifier = Modifier.fillMaxSize()
//                    )
//                } else {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Your closet looks empty!", fontSize = 14.sp)
                        Box(modifier = Modifier.height(16.dp))
                        ExtendedFloatingActionButton(
                            onClick = {
//                                launcher.launch(
//                                    PickVisualMediaRequest(
//                                        ActivityResultContracts.PickVisualMedia.ImageOnly
//                                    )
//                                )
                            },
                            text = { Text("Clothes") },
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add"
                                )
                            }
                        )
                    }
//                }
//            } else {
                Text(modifier = Modifier.align(Alignment.Center), text = "Error State")
//            }
        }
    }
}