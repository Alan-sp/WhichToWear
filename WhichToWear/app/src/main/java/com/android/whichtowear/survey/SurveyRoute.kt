package com.android.whichtowear.survey

import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.fragment.app.FragmentManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.whichtowear.db.entity.Clothing
//<<<<<<< HEAD
import com.android.whichtowear.ui.ImageColorPickerScreen
import com.android.whichtowear.util.hashList
//import com.google.android.material.datepicker.MaterialDatePicker
//=======
//import com.google.android.material.datepicker.MaterialDatePicker
//>>>>>>> ba7f55e (addweatherpic)
import java.util.Calendar

private const val CONTENT_ANIMATION_DURATION = 300

/**
 * Displays a [SurveyQuestionsScreen] tied to the passed [SurveyViewModel]
 */
@Composable
fun SurveyRoute(
    onSurveyComplete: () -> Unit,
    onNavUp: () -> Unit,
) {
    val viewModel: SurveyViewModel = hiltViewModel()

    val surveyScreenData = viewModel.surveyScreenData ?: return

    BackHandler {
        if (!viewModel.onBackPressed()) {
            onNavUp()
        }
    }

    SurveyQuestionsScreen(
        surveyScreenData = surveyScreenData,
        isNextEnabled = viewModel.isNextEnabled,
        cloth = Clothing(
            image = viewModel.selfieUri.toString(),
            type = viewModel.superheroResponse?.id?:0,
            warmth = viewModel.feelingAboutSelfiesResponse?:0f,
            date = viewModel.takeawayResponse?:0,
            points = hashList(viewModel.freeTimeResponse),
        ),
        onClosePressed = {
            onNavUp()
        },
        onPreviousPressed = { viewModel.onPreviousPressed() },
        onNextPressed = { viewModel.onNextPressed() },
        onDonePressed = { viewModel.onDonePressed(onSurveyComplete) },
        addClothing = viewModel::addClothing,
    ) { paddingValues ->

        val modifier = Modifier.padding(paddingValues)

        AnimatedContent(
            targetState = surveyScreenData,
            transitionSpec = {
                val animationSpec: TweenSpec<IntOffset> = tween(CONTENT_ANIMATION_DURATION)

                val direction = getTransitionDirection(
                    initialIndex = initialState.questionIndex,
                    targetIndex = targetState.questionIndex,
                )

                slideIntoContainer(
                    towards = direction,
                    animationSpec = animationSpec,
                ) togetherWith slideOutOfContainer(
                    towards = direction,
                    animationSpec = animationSpec
                )
            },
            label = "surveyScreenDataAnimation"
        ) { targetState ->

            when (targetState.surveyQuestion) {
                SurveyQuestion.FREE_TIME -> {
                    FreeTimeQuestion(
                        selectedAnswers = viewModel.freeTimeResponse,
                        onOptionSelected = viewModel::onFreeTimeResponse,
                        modifier = modifier,
                    )
                }

                SurveyQuestion.SUPERHERO -> SuperheroQuestion(
                    selectedAnswer = viewModel.superheroResponse,
                    onOptionSelected = viewModel::onSuperheroResponse,
                    modifier = modifier,
                )

//                SurveyQuestion.LAST_TAKEAWAY -> {
//                    val supportFragmentManager = getSupportFragmentManager()
//                        //LocalContext.current.findActivity().supportFragmentManager
//                    TakeawayQuestion(
//                        dateInMillis = viewModel.takeawayResponse,
//                        onClick = {
//                            showTakeawayDatePicker(
//                                date = viewModel.takeawayResponse,
//                                supportFragmentManager = supportFragmentManager,
//                                onDateSelected = viewModel::onTakeawayResponse
//                            )
//                        },
//                        modifier = modifier,
//                    )
//                }
                SurveyQuestion.LAST_TAKEAWAY -> {
                    val Context = LocalContext.current
                    TakeawayQuestion(
                        dateInMillis = viewModel.takeawayResponse,
                        onClick = {
                            showTakeawayDatePicker(
                                context = Context,
                                date = viewModel.takeawayResponse,
                                onDateSelected = viewModel::onTakeawayResponse
                            )
                        },
                        modifier = modifier,
                    )
                }

                SurveyQuestion.HOT_AND_COLD ->
                    HotAndColdQuestion(
                        value = viewModel.feelingAboutSelfiesResponse,
                        onValueChange = viewModel::onFeelingAboutSelfiesResponse,
                        modifier = modifier,
                    )

                SurveyQuestion.TAKE_SELFIE -> TakeSelfieQuestion(
                    imageUri = viewModel.selfieUri,
                    getNewImageUri = viewModel::getNewSelfieUri,
                    onPhotoTaken = viewModel::onSelfieResponse,
                    modifier = modifier,
                )

                SurveyQuestion.PICK_COLOR -> PickColor(
                    color = viewModel.color,
                    onColorChanged = viewModel::onColorResponse,
                    imageUri = viewModel.selfieUri,
                )
            }
        }
    }
}

private fun getTransitionDirection(
    initialIndex: Int,
    targetIndex: Int
): AnimatedContentTransitionScope.SlideDirection {
    return if (targetIndex > initialIndex) {
        // Going forwards in the survey: Set the initial offset to start
        // at the size of the content so it slides in from right to left, and
        // slides out from the left of the screen to -fullWidth
        AnimatedContentTransitionScope.SlideDirection.Left
    } else {
        // Going back to the previous question in the set, we do the same
        // transition as above, but with different offsets - the inverse of
        // above, negative fullWidth to enter, and fullWidth to exit.
        AnimatedContentTransitionScope.SlideDirection.Right
    }
}

//private fun showTakeawayDatePicker(
//    date: Long?,
//    supportFragmentManager: FragmentManager,
//    onDateSelected: (date: Long) -> Unit,
//) {
//    val picker = MaterialDatePicker.Builder.datePicker()
//        .setSelection(date)
//        .build()
//    picker.show(supportFragmentManager, picker.toString())
//    picker.addOnPositiveButtonClickListener {
//        picker.selection?.let {
//            onDateSelected(it)
//        }
//    }
//}

private fun showTakeawayDatePicker(
    date: Long?,
    onDateSelected: (date: Long) -> Unit,
    context: Context,
) {
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.timeInMillis
            onDateSelected(selectedDate)
        },
        date?.let { Calendar.getInstance().apply { timeInMillis = it } }?.get(Calendar.YEAR) ?: Calendar.getInstance().get(Calendar.YEAR),
        date?.let { Calendar.getInstance().apply { timeInMillis = it } }?.get(Calendar.MONTH) ?: Calendar.getInstance().get(Calendar.MONTH),
        date?.let { Calendar.getInstance().apply { timeInMillis = it } }?.get(Calendar.DAY_OF_MONTH) ?: Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.show()
}
//private tailrec fun Context.findActivity(): AppCompatActivity =
//    when (this) {
//        is AppCompatActivity -> this
//        is ContextWrapper -> this.baseContext.findActivity()
//        else -> throw IllegalArgumentException("Could not find activity!")
//    }


