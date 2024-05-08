package com.android.whichtowear.ui.Closet

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.whichtowear.toClothingList
import com.android.whichtowear.entity.Clothing
import com.android.whichtowear.repository.ClothingRespository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ClosetUiState {
    class PhotoList(val photos: List<Clothing>) : ClosetUiState()
}

@HiltViewModel
class ClosetViewModel @Inject constructor(private val repository: ClothingRespository) : ViewModel() {

    private val _uiState = MutableStateFlow<ClosetUiState>(ClosetUiState.PhotoList(emptyList()))
    val uiState: StateFlow<ClosetUiState> = _uiState

    init {
        viewModelScope.launch {
            repository.GetAll().collect {
                updateState(ClosetUiState.PhotoList(it))
            }
        }
    }

    private fun updateState(newState: ClosetUiState) {
        _uiState.value = newState
    }

    fun addPhotos(photos: List<Uri>) {
        viewModelScope.launch {
            repository.InsertAll(photos.toClothingList())
        }
    }
}