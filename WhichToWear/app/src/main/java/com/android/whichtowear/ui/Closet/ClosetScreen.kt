package com.android.whichtowear.ui.Closet

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ClosetScreen(
    uiState: ClosetUiState,
    TabUiState: Int,
    changeTabUiState: (Int) -> Unit,
    navigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            Column() {
                TopAppBar(title = { Text(text = "我的衣橱") })
                TabRow(selectedTabIndex = TabUiState) {
                    Tab(
                        selected = TabUiState == 0,
                        onClick = { changeTabUiState(0) },
                        text = { Text(text = "上衣") }
                    )
                    Tab(
                        selected = TabUiState == 1,
                        onClick = { changeTabUiState(1) },
                        text = { Text(text = " 裤子") }
                    )
                    Tab(
                        selected = TabUiState == 2,
                        onClick = { changeTabUiState(2) },
                        text = { Text(text = "鞋子") }
                    )
                }
            }
        },
        floatingActionButton = {
            if (uiState is ClosetUiState.PhotoList && uiState.photos.isNotEmpty())
                ExtendedFloatingActionButton(
                    onClick = {
                        navigate("survey")
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
            if (uiState is ClosetUiState.PhotoList) {
                if (uiState.photos.isNotEmpty()) {
                    Log.d("number", "${uiState.photos.size}")
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        content = {
                            itemsIndexed(uiState.photos) { index, photo ->
                                GlideImage(
                                    model = photo.image,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .aspectRatio(1f / 1.6f)
                                        .clip(shape = RoundedCornerShape(4.dp))
                                        .clickable {
                                            navigate("detail/${photo.id}")
                                        }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Your closet looks empty!", fontSize = 14.sp)
                        Box(modifier = Modifier.height(16.dp))
                        ExtendedFloatingActionButton(
                            onClick = {
                                navigate("survey")
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
                }
            } else {
                Text(modifier = Modifier.align(Alignment.Center), text = "Error State")
            }
        }
    }
}