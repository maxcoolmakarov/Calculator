package com.example.calculator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.calculator.data.SettingsDao
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val settingsDao: SettingsDao = SettingsDaoFake()

    @Test
    fun testPlus() {
        val viewModel = MainViewModel(settingsDao)
        viewModel.onNumberClick(2,0)
        viewModel.onOperatorClick(Operator.PLUS, 1)
        viewModel.onOperatorClick(Operator.MINUS, 1)
        viewModel.onOperatorClick(Operator.PLUS, 1)
        viewModel.onNumberClick(2, 2)
        assertEquals("4", viewModel.resultState.value)
    }

    @Test
    fun testPlusTwo() {
        val viewModel = MainViewModel(settingsDao)
        viewModel.onNumberClick(2,0)
        viewModel.onOperatorClick(Operator.PLUS, 1)
        assertEquals("2+", viewModel.resultState.value)
    }
}

class SettingsDaoFake : SettingsDao {
    private var resultPanelType: ResultPanelType = ResultPanelType.LEFT
    override suspend fun setResultPanelType(resultPanelType: ResultPanelType) {
        this.resultPanelType = resultPanelType
    }

    override suspend fun getResultPanelType(): ResultPanelType {
        return resultPanelType
    }
}