package com.android.whichtowear.ui.Wearing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.whichtowear.db.entity.Wearing
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WearingScreen(uiState: WearingUiState){
    Scaffold(
        topBar = {
            TopAppBar(title={Text(text = "穿着")})
        },
    ){
        Box(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            if (uiState is WearingUiState.WearingList) {
                if (uiState.wearings.isNotEmpty()) {
                    val wearingsMap = uiState.wearings
                    LazyColumn(
                        content = {
                            items(items = wearingsMap.entries.toList().reversed()) { entry ->
                                WearingTile(entry)
                                Box(modifier = Modifier.height(8.dp))
                            }
                        },
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            text = "Add a clothing to the wearings to appear here.",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                Text(text = "Error", fontSize = 24.sp)
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WearingTile(entry: Map.Entry<String, List<Wearing>>){
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(4.dp)) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Row {
                Box(modifier = Modifier.size(16.dp))
                Text(
                    text = entry.key,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(modifier = Modifier.size(16.dp))
            LazyRow(content = {
                itemsIndexed(entry.value.toList()) { index, outfit ->
                    val imageModifier = Modifier
                        .height(120.dp)
                        .width(80.dp)
                    if (index == 0)
                        Box(modifier = Modifier.size(16.dp))
                    else
                        Box(modifier = Modifier.size(4.dp))
                    if (outfit.image.isNotEmpty())
                        GlideImage(
                            modifier = imageModifier.clip(shape = RoundedCornerShape(4.dp)),
                            model = outfit.image,
                            contentDescription = "穿着",
                            contentScale = ContentScale.Crop
                        )
                    else
                        Box(modifier = imageModifier.background(color = Color.Gray))
                    if (index == entry.value.toList().size - 1) {
                        Box(modifier = Modifier.size(16.dp))
                    }
                }
            })
        }
    }
}

@Preview
@Composable
fun PreviewWearingScreen() {
    WearingScreen(uiState = WearingUiState.WearingList(emptyMap()))
}