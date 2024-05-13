package com.android.whichtowear.ui.Memo

import com.android.whichtowear.db.entity.Clothing

sealed class MemoUiState ()
{
    class PhotoList(val photos: List<Clothing>) : MemoUiState()
}