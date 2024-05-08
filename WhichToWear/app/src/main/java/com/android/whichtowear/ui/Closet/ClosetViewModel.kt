package com.android.whichtowear.ui.Closet

import android.net.Uri
import android.util.Log
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

sealed class ClosetUiState (var TabUiState: Int)
{
    class PhotoList(val photos: List<Clothing>) : ClosetUiState(0)
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
            Log.d("do","INIt")
        }
    }

    private fun updateState(newState: ClosetUiState) {
        _uiState.value = newState
    }
    fun changeTabUiState(newState: Int){
        _uiState.value.TabUiState = newState
    }

    fun addPhotos(photos: List<Uri>) {
        viewModelScope.launch {
            repository.InsertAll(photos.toClothingList())
        }
    }
}