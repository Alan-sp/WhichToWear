package com.android.whichtowear.ui.survey

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import com.android.whichtowear.R
import com.android.whichtowear.ui.survey.question.DateQuestion
import com.android.whichtowear.ui.survey.question.MultipleChoiceQuestion
import com.android.whichtowear.ui.survey.question.PhotoQuestion
import com.android.whichtowear.ui.survey.question.SingleChoiceQuestion
import com.android.whichtowear.ui.survey.question.SliderQuestion
import com.android.whichtowear.ui.survey.question.Superhero
import com.android.whichtowear.ui.ImageColorPickerScreen
import com.android.whichtowear.ui.Main.MainUiState.Closet.icon
import com.github.skydoves.colorpicker.compose.ColorEnvelope

@Composable
fun FreeTimeQuestion(
    selectedAnswers: List<Int>,
    onOptionSelected: (selected: Boolean, answer: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    MultipleChoiceQuestion(
        titleResourceId = R.string.in_my_free_time,
        directionsResourceId = R.string.select_all,
        possibleAnswers = listOf(
            R.string.read,
            R.string.work_out,
            R.string.draw,
            R.string.wind,
            R.string.play_games,
            R.string.dance,
            R.string.watch_movies,
        ),
        selectedAnswers = selectedAnswers,
        onOptionSelected = onOptionSelected,
        modifier = modifier,
    )
}

@Composable
fun SuperheroQuestion(
    selectedAnswer: Superhero?,
    onOptionSelected: (Superhero) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceQuestion(
        titleResourceId = R.string.pick_superhero,
        directionsResourceId = R.string.select_one,
        possibleAnswers = listOf(
            Superhero(0,R.string.shirts, R.drawable.t_shirt),
            Superhero(1,R.string.pants, R.drawable.shorts),
            Superhero(2,R.string.shoes, R.drawable.slippers),
        ),
        selectedAnswer = selectedAnswer,
        onOptionSelected = onOptionSelected,
        modifier = modifier,
    )
}

@Composable
fun TakeawayQuestion(
    dateInMillis: Long?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DateQuestion(
        titleResourceId = R.string.takeaway,
        directionsResourceId = R.string.select_date,
        dateInMillis = dateInMillis,
        onClick = onClick,
        modifier = modifier,
    )
}
@Composable
fun HotAndColdQuestion(
    value: Float?,
    onValueChange: (Float)->Unit,
    modifier: Modifier = Modifier,
){
    SliderQuestion(
        titleResourceId = R.string.HotAndCold,
        value = value,
        onValueChange = onValueChange,
        startTextResource = R.string.Hot,
        neutralTextResource =R.string.Medium,
        endTextResource = R.string.Cold,
        modifier = modifier,
    )
}

@Composable
fun TakeSelfieQuestion(
    imageUri: Uri?,
    getNewImageUri: () -> Uri,
    onPhotoTaken: (Uri) -> Unit,
    modifier: Modifier = Modifier,
) {
    PhotoQuestion(
        titleResourceId = R.string.selfie_skills,
        imageUri = imageUri,
        getNewImageUri = getNewImageUri,
        onPhotoTaken = onPhotoTaken,
        modifier = modifier,
    )
}

@Composable
fun PickColor(
    color : ColorEnvelope?,
    onColorChanged: (colorEnvelope: ColorEnvelope) -> Unit,
    imageUri: Uri?,
    modifier: Modifier,
){
    ImageColorPickerScreen(
        titleResourceId = R.string.get_color,
        color = color,
        onColorChanged = onColorChanged,
        imageUri = imageUri,
        modifier = modifier,
    )
}