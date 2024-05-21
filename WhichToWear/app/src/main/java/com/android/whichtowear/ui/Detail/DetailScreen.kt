 package com.android.whichtowear.ui.Detail

import android.content.Context
import android.service.autofill.OnClickAction
import android.util.Log
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
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import com.android.whichtowear.util.getDateFormat
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.launch
import java.util.Date

 @OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: DetailUiState,
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
                title = { Text(text = "详细信息") },
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
                DetailScreenBottomAppBar(context, uiState.clothing,)
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
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(4.dp)) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = getPoints(clothing),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(text = "特性", fontSize = 12.sp)
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = getDateFormat(clothing.date),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(text = "购入时间", fontSize = 12.sp)
                            }
                        }
                    }
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
                    Text(text = "确定要删除这件衣服吗？")
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
                                Toast.makeText(context, "已完成删除", Toast.LENGTH_SHORT)
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
) {
    BottomAppBar(
        actions = {
//            FilledTonalButton(
//                onClick = {
//                }
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.closet),
//                    contentDescription = "Add to outfit"
//                )
//                Box(modifier = Modifier.width(4.dp))
//                Text(text = "Add to outfit")
//            }
//            Box(modifier = Modifier.width(16.dp))
//            FilledTonalButton(
//                onClick = {
//                }
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.laundry),
//                    contentDescription = "Add to laundry"
//                )
//                Box(modifier = Modifier.width(4.dp))
//                Text(text = "Add to laundry")
//            }
        },
//
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

fun getPoints(clothing: Clothing):String
{
    var points = ""
    Log.d("DEBUG",clothing.points.toString())
    if(clothing.points.and(1) == 1)
    {
        points += R.string.read
    }
    if(clothing.points.and(2) == 2)
    {
        if(points.isNotEmpty())
        {
            points += "、"
        }
        points += R.string.work_out
    }
    if(clothing.points.and(4) == 4)
    {
        if(points.isNotEmpty())
        {
            points += "、"
        }
        points += R.string.draw
    }
    if(clothing.points.and(8) == 8)
    {
        if(points.isNotEmpty())
        {
            points += "、"
        }
        points += R.string.wind
    }
    if(clothing.points.and(16) == 16)
    {
        if(points.isNotEmpty())
        {
            points += "、"
        }
        points += R.string.play_games
    }
    if(clothing.points.and(32) == 32)
    {
        if(points.isNotEmpty())
        {
            points += "、"
        }
        points += R.string.dance
    }
    if(clothing.points.and(64) == 64)
    {
        if(points.isNotEmpty())
        {
            points += "、"
        }
        points += R.string.watch_movies
    }
    return points
}