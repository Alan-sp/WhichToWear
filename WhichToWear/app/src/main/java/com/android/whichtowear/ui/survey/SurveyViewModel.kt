package com.android.whichtowear.ui.survey

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.whichtowear.db.entity.Clothing
import com.android.whichtowear.db.repository.ClothingRespository
import com.android.whichtowear.ui.survey.question.Superhero
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


const val simpleDateFormatPattern = "EEE, MMM d"

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val repository: ClothingRespository,
    private val photoUriManager: PhotoUriManager
) : ViewModel() {

    private val questionOrder: List<SurveyQuestion> = listOf(
        SurveyQuestion.TAKE_SELFIE,
        SurveyQuestion.PICK_COLOR,
        SurveyQuestion.SUPERHERO,
        SurveyQuestion.FREE_TIME,
        SurveyQuestion.LAST_TAKEAWAY,
        SurveyQuestion.HOT_AND_COLD,
    )

    private var questionIndex = 0

    // ----- Responses exposed as State -----

    private val _freeTimeResponse = mutableStateListOf<Int>()
    val freeTimeResponse: List<Int>
        get() = _freeTimeResponse

    private val _superheroResponse = mutableStateOf<Superhero?>(null)
    val superheroResponse: Superhero?
        get() = _superheroResponse.value

    private val _takeawayResponse = mutableStateOf<Long?>(null)
    val takeawayResponse: Long?
        get() = _takeawayResponse.value

    private val _feelingAboutSelfiesResponse = mutableStateOf<Float?>(null)
    val feelingAboutSelfiesResponse: Float?
        get() = _feelingAboutSelfiesResponse.value

    private val _selfieUri = mutableStateOf<Uri?>(null)
    val selfieUri
        get() = _selfieUri.value

    private val _color = mutableStateOf<ColorEnvelope?>(null)
    val color
        get() = _color.value

    // ----- Survey status exposed as State -----

    private val _surveyScreenData = mutableStateOf(createSurveyScreenData())
    val surveyScreenData: SurveyScreenData?
        get() = _surveyScreenData.value

    private val _isNextEnabled = mutableStateOf(false)
    val isNextEnabled: Boolean
        get() = _isNextEnabled.value

    /**
     * Returns true if the ViewModel handled the back press (i.e., it went back one question)
     */
    fun onBackPressed(): Boolean {
        if (questionIndex == 0) {
            return false
        }
        changeQuestion(questionIndex - 1)
        return true
    }

    fun onPreviousPressed() {
        if (questionIndex == 0) {
            throw IllegalStateException("onPreviousPressed when on question 0")
        }
        changeQuestion(questionIndex - 1)
    }

    fun onNextPressed() {
        changeQuestion(questionIndex + 1)
    }

    private fun changeQuestion(newQuestionIndex: Int) {
        questionIndex = newQuestionIndex
        _isNextEnabled.value = getIsNextEnabled()
        _surveyScreenData.value = createSurveyScreenData()
    }

    fun onDonePressed(onSurveyComplete: () -> Unit) {
        onSurveyComplete()
    }

    fun onFreeTimeResponse(selected: Boolean, answer: Int) {
        if (selected) {
            _freeTimeResponse.add(answer)
        } else {
            _freeTimeResponse.remove(answer)
        }
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onSuperheroResponse(superhero: Superhero) {
        _superheroResponse.value = superhero
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onTakeawayResponse(timestamp: Long) {
        _takeawayResponse.value = timestamp
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onFeelingAboutSelfiesResponse(feeling: Float) {
        _feelingAboutSelfiesResponse.value = feeling
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onSelfieResponse(uri: Uri) {
        _selfieUri.value = uri
        _isNextEnabled.value = getIsNextEnabled()
    }
    fun onColorResponse(color: ColorEnvelope){
        _color.value = color
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun addClothing(clothing: Clothing) {
        viewModelScope.launch {
            repository.InsertAll(
                listOf(clothing)
            )
        }
    }

    fun getNewSelfieUri() = photoUriManager.buildNewUri()

    private fun getIsNextEnabled(): Boolean {
        return when (questionOrder[questionIndex]) {
            SurveyQuestion.FREE_TIME -> _freeTimeResponse.isNotEmpty()
            SurveyQuestion.SUPERHERO -> _superheroResponse.value != null
            SurveyQuestion.LAST_TAKEAWAY -> _takeawayResponse.value != null
            SurveyQuestion.HOT_AND_COLD -> _feelingAboutSelfiesResponse.value != null
            SurveyQuestion.TAKE_SELFIE -> _selfieUri.value != null
            SurveyQuestion.PICK_COLOR -> _color.value !=null
        }
    }

    private fun createSurveyScreenData(): SurveyScreenData {
        return SurveyScreenData(
            questionIndex = questionIndex,
            questionCount = questionOrder.size,
            shouldShowPreviousButton = questionIndex > 0,
            shouldShowDoneButton = questionIndex == questionOrder.size - 1,
            surveyQuestion = questionOrder[questionIndex],
        )
    }
}

enum class SurveyQuestion {
    FREE_TIME,
    SUPERHERO,
    LAST_TAKEAWAY,
    HOT_AND_COLD,
    TAKE_SELFIE,
    PICK_COLOR
}

data class SurveyScreenData(
    val questionIndex: Int,
    val questionCount: Int,
    val shouldShowPreviousButton: Boolean,
    val shouldShowDoneButton: Boolean,
    val surveyQuestion: SurveyQuestion,
)