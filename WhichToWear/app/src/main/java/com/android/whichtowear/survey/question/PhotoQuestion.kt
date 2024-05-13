package com.android.whichtowear.survey.question

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.whichtowear.R
import com.android.whichtowear.survey.QuestionWrapper
import com.android.whichtowear.ui.theme.WhichToWearTheme
import timber.log.Timber

@Composable
fun PhotoQuestion(
    @StringRes titleResourceId: Int,
    imageUri: Uri?,
    getNewImageUri: () -> Uri,
    onPhotoTaken: (Uri) -> Unit,
    addPhotos: (List<Uri?>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(10)
    ) { uris ->
        if (uris.isNotEmpty()) {
            Timber.d("Number of items selected: ${uris.size}")
            for (uri in uris)
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            onPhotoTaken(uris[0])
            addPhotos(uris)
        } else {
            Timber.d("No media selected")
        }
    }
    val hasPhoto = imageUri != null
    val iconResource = if (hasPhoto) {
        Icons.Filled.SwapHoriz
    } else {
        Icons.Filled.AddAPhoto
    }
    var newImageUri: Uri? = null

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onPhotoTaken(newImageUri!!)
                addPhotos(listOf(newImageUri))
            }
        }
    )

    QuestionWrapper(
        titleResourceId = titleResourceId,
        modifier = modifier,
    ) {
        Column{
            if (hasPhoto) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(96.dp)
                        .aspectRatio(4 / 3f)
                )
            } else {
                PhotoDefaultImage(
                    modifier = Modifier.padding(
                        horizontal = 86.dp,
                        vertical = 74.dp
                    )
                )
            }
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.BottomCenter)
                    .padding(vertical = 26.dp),
                onClick = {
                    newImageUri = getNewImageUri()
                    cameraLauncher.launch(newImageUri)
                },
            ) {
                Icon(imageVector = iconResource, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(
                        id = if (hasPhoto) {
                            R.string.retake_photo
                        } else {
                            R.string.add_photo
                        }
                    )
                )
            }
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.BottomCenter)
                    .padding(vertical = 26.dp),
                onClick = {
                    launcher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
            ) {
                Icon(imageVector = iconResource, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(
                        id = if (hasPhoto) {
                            R.string.retake_photo
                        } else {
                            R.string.add_photo
                        }
                    )
                )
            }
        }
    }
}

@Composable
private fun PhotoDefaultImage(
    modifier: Modifier = Modifier,
    lightTheme: Boolean = LocalContentColor.current.luminance() < 0.5f,
) {
    val assetId = if (lightTheme) {
        R.drawable.ic_launcher_background
    } else {
        R.drawable.ic_launcher_foreground
    }
    Image(
        painter = painterResource(id = assetId),
        modifier = modifier,
        contentDescription = null
    )
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PhotoQuestionPreview() {
    WhichToWearTheme {
        Surface {
            PhotoQuestion(
                titleResourceId = R.string.selfie_skills,
                imageUri = Uri.parse("https://example.bogus/wow"),
                getNewImageUri = { Uri.EMPTY },
                onPhotoTaken = {},
                addPhotos = {},
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}