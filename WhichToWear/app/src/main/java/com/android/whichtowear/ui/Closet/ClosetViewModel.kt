package com.android.whichtowear.ui.Closet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.whichtowear.db.entity.Clothing
import com.android.whichtowear.db.repository.ClothingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ClosetUiState ()
{
    class PhotoList(val photos: List<Clothing>) : ClosetUiState()
}

@HiltViewModel
class ClosetViewModel @Inject constructor(private val repository: ClothingRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ClosetUiState>(ClosetUiState.PhotoList(emptyList()))
    val uiState: StateFlow<ClosetUiState> = _uiState

    private val _TabUiState = MutableLiveData<Int>(0)
    val TabUiState: LiveData<Int> = _TabUiState

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
    fun changeTabUiState(newState: Int){
        _TabUiState.value = newState
    }
}