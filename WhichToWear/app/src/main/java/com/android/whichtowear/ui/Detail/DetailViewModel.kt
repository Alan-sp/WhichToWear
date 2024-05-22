package com.android.whichtowear.ui.Detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.whichtowear.db.entity.Clothing
import com.android.whichtowear.db.entity.Wearing
import com.android.whichtowear.db.repository.DetailRepository
import com.android.whichtowear.db.repository.WearingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DetailUiState {
    object Error : DetailUiState()
    data class OpenDetail(
        val clothing: Clothing,
        //val laundryCount: Int? = null,
        //val lastLaundryDate: Long? = null
    ) : DetailUiState()
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val wearingRepository: WearingRepository,
    private val detailRepository: DetailRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Error)
    val uiState = _uiState.asStateFlow()

    private val clothingId: Int = checkNotNull(savedStateHandle["clothId"])

    init {
        viewModelScope.launch {
            val clothing = detailRepository.get(clothingId)
            updateState(DetailUiState.OpenDetail(clothing))
        }
    }

    private fun updateState(newState: DetailUiState) {
        _uiState.value = newState
    }

    fun addToWearing(clothing: Clothing, onComplete: () -> Unit) {
        viewModelScope.launch {
            val wear = Wearing(clothingId = clothing.id, image = clothing.image)
            wearingRepository.InsertAll(listOf(wear))
            onComplete()
        }
    }

//    fun addToLaundry(onComplete: () -> Unit) {
//        viewModelScope.launch {
//            detailRepository.addLaundry(clothingId)
//            onComplete()
//        }
//    }

    fun delete(onDelete: () -> Unit) {
        viewModelScope.launch {
            detailRepository.deleteClothing(clothingId)
            onDelete()
        }
    }

}