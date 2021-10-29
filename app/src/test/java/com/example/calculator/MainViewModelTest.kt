package com.example.calculator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun testPlus() {
        val viewModel = MainViewModel()
        viewModel.onNumberClick(2,0)
        viewModel.onOperatorClick(Operator.PLUS, 1)
        viewModel.onOperatorClick(Operator.MINUS, 1)
        viewModel.onOperatorClick(Operator.PLUS, 1)
        viewModel.onNumberClick(2, 2)
        assertEquals("4", viewModel.resultState.value)
    }

    @Test
    fun testPlusTwo() {
        val viewModel = MainViewModel()
        viewModel.onNumberClick(2,0)
        viewModel.onOperatorClick(Operator.PLUS, 1)
        assertEquals("2+", viewModel.resultState.value)
    }
}