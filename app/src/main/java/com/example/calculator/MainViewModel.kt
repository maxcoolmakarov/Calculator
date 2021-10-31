package com.example.calculator

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.calculator.data.SettingsDao
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsDao: SettingsDao
) : ViewModel() {

    private var expression: String = ""
    private val _expressionState = MutableLiveData(ExpressionState(expression, 0))
    val expressionState: LiveData<ExpressionState> = _expressionState

    private val _resultState = MutableLiveData<String>()
    val resultState: LiveData<String> = _resultState

    private val _resultPanelState = MutableLiveData<ResultPanelType>()
    val resultPanelState: LiveData<ResultPanelType> = _resultPanelState

    init {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MainViewModel", "OnCleared")
    }
    fun onNumberClick(number: Int, selection: Int){
        expression = putInSelection(expression, number.toString(), selection, false)
        _expressionState.value = ExpressionState(expression, selection + 1)
        _resultState.value = expressionCalculator(expression)
    }

    fun onOperatorClick(operator: Operator, selection: Int) {
        val len = expression.length
        expression = putInSelection(expression, operator.symbol, selection, true)
        if (len == expression.length) {
            _expressionState.value = ExpressionState(expression, selection)
        } else {
            _expressionState.value = ExpressionState(expression, selection + 1)
        }
        _resultState.value = expressionCalculator(expression)

    }

    fun onClearChar(selection: Int) {
        if (expression.isNotEmpty()) {
            expression =  expression.dropLast(1)
            _expressionState.value = ExpressionState(expression, selection - 1)
            _resultState.value = expressionCalculator(expression)
        }
    }

    fun onClear() {
        expression =  ""
        _expressionState.value = ExpressionState(expression, 0)
        _resultState.value = ""
    }

    fun onResult() {
        val result = expressionCalculator(expression)
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
    }


}

enum class Operator(val symbol: String) {
    PLUS("+"), MINUS("-"), MULTIPLY("*"), DEVIDE("/"), DOT(".")
}

class ExpressionState(val expression: String, val selection: Int)