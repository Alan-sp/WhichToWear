package com.android.whichtowear.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.whichtowear.R
import com.android.whichtowear.ui.survey.ColorPicker.PhotoPickerIcon
import com.android.whichtowear.ui.survey.QuestionWrapper
import com.android.whichtowear.ui.theme.WhichToWearTheme
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ImageColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.FileInputStream

@Composable
fun ImageColorPickerScreen(
    @StringRes titleResourceId: Int,
    color : ColorEnvelope?,
    onColorChanged: (colorEnvelope: ColorEnvelope) -> Unit,
    imageUri: Uri?,
    modifier: Modifier = Modifier,
) {
    val controller = rememberColorPickerController()
    val context = LocalContext.current
    var color_E by remember {
        mutableStateOf(color?:ColorEnvelope(Color.White,"#FFFFFF",false))
    }
    var isLoaded by remember { mutableStateOf(false) }
    var paletteImageBitmap by remember(imageUri) { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(imageUri) {
        if (imageUri != null) {
            val bitmap = withContext(Dispatchers.IO) {
                loadBitmapFromFile(context, imageUri)
            }
            paletteImageBitmap = bitmap
        }
        isLoaded = true
    }


    QuestionWrapper(
        titleResourceId = titleResourceId,
        modifier = modifier,
    ){
        if(isLoaded){
            Column {
                PhotoPickerIcon(controller)


                val maxWidth = 400.dp
                val maxHeight = 400.dp
                paletteImageBitmap?.let { bitmap ->
                    val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()

                    val calculatedWidth = (maxWidth.value * aspectRatio).coerceAtMost(maxWidth.value)
                    val calculatedHeight = (maxHeight.value / aspectRatio).coerceAtMost(maxHeight.value)

                    val finalWidth = calculatedWidth.dp.coerceAtMost(maxWidth)
                    val finalHeight = calculatedHeight.dp.coerceAtMost(maxHeight)
                    ImageColorPicker(
                        modifier = Modifier
                            .width(finalWidth)
                            .height(finalHeight)
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally),
                        controller = controller,
                        paletteImageBitmap = bitmap,
                        onColorChanged = {
                            color_E = it
                            onColorChanged(color_E!!)
                        }
                    )
                }

                //Spacer(modifier = Modifier.weight(5f))

                AlphaSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(35.dp)
                        .align(Alignment.CenterHorizontally),
                    controller = controller,
                )

                BrightnessSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(35.dp)
                        .align(Alignment.CenterHorizontally),
                    controller = controller,
                )

                //Spacer(modifier = Modifier.weight(3f))

                Text(
                    //text = "#$hexCode",
                    //color = textColor,
                    text = "#${color_E!!.hexCode}",
                    color = color_E!!.color,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )

                AlphaTile(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .align(Alignment.CenterHorizontally),
                    controller = controller,
                )

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
        else {
            LoadingScreen()
        }
    }
}

suspend fun loadBitmapFromFile(context: Context, uri: Uri): ImageBitmap {
    return withContext(Dispatchers.IO) {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor?.fileDescriptor
        val fileInputStream = FileInputStream(fileDescriptor)
        val bitmap = BitmapFactory.decodeStream(fileInputStream)
        parcelFileDescriptor?.close()
        bitmap.asImageBitmap()
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(70.dp) // 设置尺寸
                .align(Alignment.Center), // 设置边距
            color = Color.DarkGray
        )
    }
}

@Preview
@Composable
fun ImageColorPickerPreview(){
    WhichToWearTheme {
        Surface {
            ImageColorPickerScreen (
                titleResourceId = R.string.get_color,
                color = ColorEnvelope(Color.White,"#FFFFFF",false),
                onColorChanged = {},
                imageUri = Uri.parse("https://example.bogus/wow"),
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}