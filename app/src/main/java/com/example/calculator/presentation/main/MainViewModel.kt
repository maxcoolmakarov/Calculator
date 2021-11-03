package com.example.calculator

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.calculator.domain.HistoryRepository
import com.example.calculator.domain.SettingsDao
import com.example.calculator.presentation.history.HistoryItem
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsDao: SettingsDao,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private var expression: String = ""
    private val _expressionState = MutableLiveData(ExpressionState(expression, 0))
    val expressionState: LiveData<ExpressionState> = _expressionState

    private val _resultState = MutableLiveData<String>()
    val resultState: LiveData<String> = _resultState

    private val _resultPanelState = MutableLiveData<ResultPanelType>()
    val resultPanelState: LiveData<ResultPanelType> = _resultPanelState

    private val _resultAccuracy = MutableLiveData<Int>()
    val resultAccuracy = _resultAccuracy

    init {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
        }
        _resultAccuracy.value = settingsDao.getResultAccuracy()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MainViewModel", "OnCleared")
    }

    fun update() {
        _resultState.value = expressionCalculator(expression, _resultAccuracy.value?:0)
    }

    fun onNumberClick(number: Int, selection: Int){
        expression = putInSelection(expression, number.toString(), selection, false)
        _expressionState.value = ExpressionState(expression, selection + 1)
        _resultState.value = expressionCalculator(expression, _resultAccuracy.value?:0)
    }

    fun onOperatorClick(operator: Operator, selection: Int) {
        val len = expression.length
        expression = putInSelection(expression, operator.symbol, selection, true)
        if (len == expression.length) {
            _expressionState.value = ExpressionState(expression, selection)
        } else {
            _expressionState.value = ExpressionState(expression, selection + 1)
        }
        _resultState.value = expressionCalculator(expression, _resultAccuracy.value?:0)

    }

    fun onClearChar(selection: Int) {
        if (expression.isNotEmpty()) {
            expression =  expression.dropLast(1)
            _expressionState.value = ExpressionState(expression, selection - 1)
            _resultState.value = expressionCalculator(expression, _resultAccuracy.value?:0)
        }
    }

    fun onClear() {
        expression =  ""
        _expressionState.value = ExpressionState(expression, 0)
        _resultState.value = ""
    }

    fun onResult() {
        val result = expressionCalculator(expression, _resultAccuracy.value?:0)

        viewModelScope.launch {
            historyRepository.add(HistoryItem(expression, result))
        }
        expression = result
        _expressionState.value = ExpressionState(result, result.length)
        _resultState.value = ""
    }

    private fun putInSelection(expression: String, put: String, selection: Int, sign: Boolean): String {
        var begin = expression.substring(0, selection)
        val illegalChar = "+-/*"
        var end = expression.substring(selection, expression.length)
        if (sign) {
            begin = begin.dropLastWhile { a -> illegalChar.contains(a)  }
            end = end.dropWhile { a -> illegalChar.contains(a) }
        }
        return begin + put + end
    }

    fun onStart() {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
        }
        _resultAccuracy.value = settingsDao.getResultAccuracy()
    }

    fun onHistoryResult(item: HistoryItem?) {
        if (item != null) {
            expression = item.expression
            _expressionState.value = ExpressionState(expression, expression.length)
            _resultState.value = item.result
        }
    }


}

enum class Operator(val symbol: String) {
    PLUS("+"), MINUS("-"), MULTIPLY("*"), DEVIDE("/"), DOT(".")
}

class ExpressionState(val expression: String, val selection: Int)