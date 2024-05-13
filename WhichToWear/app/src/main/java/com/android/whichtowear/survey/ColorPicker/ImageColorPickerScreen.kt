package com.android.whichtowear.ui

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.Coil
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.android.whichtowear.R
import com.android.whichtowear.survey.ColorPicker.PhotoPickerIcon
import com.android.whichtowear.ui.theme.WhichToWearTheme
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ImageColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController


@Composable
fun AsyncImageToBitmap(imageUri: Uri?): ImageBitmap? {
    var imageBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUri)
            .build()
    )

    LaunchedEffect(painter) {
        val state = painter.state
        if (state is AsyncImagePainter.State.Success) {
            imageBitmap = state.result.drawable.toBitmap().asImageBitmap()
        }
    }
    return imageBitmap
}

@Composable
fun ImageColorPickerScreen(
    color : ColorEnvelope?,
    onColorChanged: (colorEnvelope: ColorEnvelope) -> Unit,
    imageUri: Uri?
) {
    val controller = rememberColorPickerController()
    val context = LocalContext.current
    var color_E by remember {
        mutableStateOf(color?:ColorEnvelope(Color.White,"#FFFFFF",false))
    }

    Column {
        Spacer(modifier = Modifier.weight(1f))

        PhotoPickerIcon(controller)

        ImageColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(10.dp),
            controller = controller,
            //paletteImageBitmap = ImageBitmap.imageResource(R.drawable.frag),
            paletteImageBitmap = AsyncImageToBitmap(imageUri= imageUri)?: ImageBitmap(1,1),
            onColorChanged = {
                color_E = it
                onColorChanged(color_E!!)
            },
        )

        Spacer(modifier = Modifier.weight(5f))

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

        Spacer(modifier = Modifier.weight(3f))

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

@Preview
@Composable
fun ImageColorPickerPreview(){
    WhichToWearTheme {
        Surface {
            ImageColorPickerScreen (
                color = ColorEnvelope(Color.White,"#FFFFFF",false),
                onColorChanged = {},
                imageUri = Uri.parse("https://example.bogus/wow"),
            )
        }
    }
}