package com.android.whichtowear.ui.Main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class MainUiState(val route:String,val icon:Int)
{
//    companion object{
    object Closet : MainUiState("closet",1)
    object Suit : MainUiState("suit",1)
    object Outfit : MainUiState("outfit",1)
//    }
}
@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel()
{
    private val _uiState = MutableLiveData<MainUiState>(MainUiState.Closet)
    val uiState : LiveData<MainUiState>
        get() = _uiState
    fun updateUiState(newState:MainUiState)
    {
        _uiState.value = newState
    }
}