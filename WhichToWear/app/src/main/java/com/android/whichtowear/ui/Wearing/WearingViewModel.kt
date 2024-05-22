package com.android.whichtowear.ui.Wearing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.whichtowear.db.entity.Wearing
import com.android.whichtowear.db.repository.WearingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WearingUiState{
    data class WearingList(val wearings: Map<String, List<Wearing>>): WearingUiState()
}

@HiltViewModel
class WearingViewModel @Inject constructor(
    private val wearingRepository: WearingRepository
): ViewModel(){
    private val _uistate = MutableStateFlow<WearingUiState>(WearingUiState.WearingList(emptyMap()))
    val uiState = _uistate.asStateFlow()

    init{
        viewModelScope.launch{
            wearingRepository.GetAllAsMap().collect{
                updateState(WearingUiState.WearingList(it))
            }
        }
    }

    private fun updateState(newState: WearingUiState){
        _uistate.value = newState
    }
}