package com.android.whichtowear.ui.survey

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.whichtowear.R
import com.android.whichtowear.db.entity.Clothing
import com.android.whichtowear.ui.theme.stronglyDeemphasizedAlpha
import com.android.whichtowear.util.supportWideScreen

@Composable
fun SurveyQuestionsScreen(
    surveyScreenData: SurveyScreenData,
    isNextEnabled: Boolean,
    cloth:Clothing,
    onClosePressed: () -> Unit,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit,
    addClothing: (Clothing) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {

    Surface(modifier = Modifier.supportWideScreen()) {
        Scaffold(
            topBar = {
                SurveyTopAppBar(
                    questionIndex = surveyScreenData.questionIndex,
                    totalQuestionsCount = surveyScreenData.questionCount,
                    onClosePressed = onClosePressed,
                )
            },
            content = content,
            bottomBar = {
                SurveyBottomBar(
                    cloth = cloth,
                    addClothing = addClothing,
                    shouldShowPreviousButton = surveyScreenData.shouldShowPreviousButton,
                    shouldShowDoneButton = surveyScreenData.shouldShowDoneButton,
                    isNextButtonEnabled = isNextEnabled,
                    onPreviousPressed = onPreviousPressed,
                    onNextPressed = onNextPressed,
                    onDonePressed = onDonePressed
                )
            }
        )
    }
}

@Composable
private fun TopAppBarTitle(
    questionIndex: Int,
    totalQuestionsCount: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = (questionIndex + 1).toString(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = stronglyDeemphasizedAlpha)
        )
        Text(
            text = stringResource(R.string.question_count, totalQuestionsCount),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class) // CenterAlignedTopAppBar is experimental in m3
@Composable
fun SurveyTopAppBar(
    questionIndex: Int,
    totalQuestionsCount: Int,
    onClosePressed: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        CenterAlignedTopAppBar(
            title = {
                TopAppBarTitle(
                    questionIndex = questionIndex,
                    totalQuestionsCount = totalQuestionsCount,
                )
            },
            actions = {
                IconButton(
                    onClick = onClosePressed,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close),
                        tint = MaterialTheme.colorScheme.onSurface.copy(stronglyDeemphasizedAlpha)
                    )
                }
            }
        )

        val animatedProgress by animateFloatAsState(
            targetValue = (questionIndex + 1) / totalQuestionsCount.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        )
    }
}

@Composable
fun SurveyBottomBar(
    cloth:Clothing,
    addClothing: (Clothing) -> Unit,
    shouldShowPreviousButton: Boolean,
    shouldShowDoneButton: Boolean,
    isNextButtonEnabled: Boolean,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit
) {
    Surface(shadowElevation = 7.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom))
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            if (shouldShowPreviousButton) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onPreviousPressed
                ) {
                    Text(text = stringResource(id = R.string.previous))
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            if (shouldShowDoneButton) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick =
                    {
                        addClothing(cloth)
                        onDonePressed()
                    },
                    enabled = isNextButtonEnabled,
                ) {
                    Text(text = stringResource(id = R.string.done))
                }
            } else {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onNextPressed,
                    enabled = isNextButtonEnabled,
                ) {
                    Text(text = stringResource(id = R.string.next))
                }
            }
        }
    }
}
