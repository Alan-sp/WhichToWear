package com.android.whichtowear.survey.ColorPicker

import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.os.BuildCompat
import com.android.whichtowear.R
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.google.modernstorage.photopicker.PhotoPicker


@OptIn(BuildCompat.PrereleaseSdkCheck::class)
@Composable
@SuppressLint("UnsafeOptInUsageError")
fun ColumnScope.PhotoPickerIcon(
    controller: ColorPickerController,
) {
    val context = LocalContext.current
    val photoPicker =
        rememberLauncherForActivityResult(PhotoPicker()) { uris ->
            val uri = uris.firstOrNull() ?: return@rememberLauncherForActivityResult

            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }

            controller.setPaletteImageBitmap(bitmap.asImageBitmap())
        }

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .align(Alignment.End),
    ) {
        Image(
            modifier = Modifier
                .size(42.dp)
                .clickable {
                    // Launch the picker with only one image selectable
                    photoPicker.launch(PhotoPicker.Args(PhotoPicker.Type.IMAGES_ONLY, 1))
                },
            imageVector = ImageVector.vectorResource(R.drawable.ic_gallery),
            contentDescription = null,
        )
    }
}