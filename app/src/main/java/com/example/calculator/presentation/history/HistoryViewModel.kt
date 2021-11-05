package com.example.calculator.presentation.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.domain.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _closeWithResult = MutableLiveData<HistoryItem>()
    val closeWithResult = _closeWithResult

    private val _historyItemsState = MutableLiveData<List<HistoryItem>>()
    val historyItemsState = _historyItemsState

    init {
        viewModelScope.launch {
            _historyItemsState.value = historyRepository.getAll()
        }

    }

    fun onItemClicked(historyItem: HistoryItem) {
        _closeWithResult.value = historyItem
    }

    fun onClearClicked() {
        viewModelScope.launch {
            historyRepository.deleteAll()
            _historyItemsState.value = historyRepository.getAll()
        }

    }


}

