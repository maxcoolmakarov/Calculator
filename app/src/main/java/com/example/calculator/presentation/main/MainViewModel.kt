package com.example.calculator

import android.content.Context
import android.os.Vibrator
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemServiceName
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

    private val _vibrationForce = MutableLiveData<Int>()
    val vibrationForce = _vibrationForce



    init {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
        }
        _resultAccuracy.value = settingsDao.getResultAccuracy()
        _vibrationForce.value = settingsDao.getVibrationForce()
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
            val begin = expression.substring(0, selection)
            val end = expression.substring(selection, expression.length)
            expression =  begin.dropLast(1) + end
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
        if (result.isNotEmpty()) {
            viewModelScope.launch {
                historyRepository.add(HistoryItem(expression, result))
            }
            expression = result
            _expressionState.value = ExpressionState(result, result.length)
            _resultState.value = ""
        }
    }

    private fun putInSelection(expression: String, put: String, selection: Int, sign: Boolean): String {
        var begin = expression.substring(0, selection)
        val illegalChar = "+-/*^"
        var end = expression.substring(selection, expression.length)
        if (sign) {
            begin = begin.dropLastWhile { a -> illegalChar.contains(a)  }
            end = end.dropWhile { a -> illegalChar.contains(a) }
        }
        return begin + put + end
    }

    private fun setBracket(expression: String, selection: Int):String {
        var begin: String = expression.substring(0, selection)
        val legalChar = "+-/*^"
        var end: String = expression.substring(selection, expression.length)
        return if (legalChar.contains(begin.lastOrNull()?:'+') || begin.lastOrNull() == '(')
            "$begin($end"
            else
            "$begin)$end"
    }

    fun onBracketClick(selection: Int){
        expression = setBracket(expression, selection)
        _expressionState.value = ExpressionState(expression, selection + 1)
        _resultState.value = expressionCalculator(expression, _resultAccuracy.value?:0)
    }

    fun onStart() {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
        }
        _resultAccuracy.value = settingsDao.getResultAccuracy()

        _vibrationForce.value = settingsDao.getVibrationForce()
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
    PLUS("+"), MINUS("-"), MULTIPLY("*"), DEVIDE("/"), DOT("."), POW("^")
}

class ExpressionState(val expression: String, val selection: Int)