package com.example.calculator

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

class ExpressionCalculatorKtTest {

    @Test
    fun testPlus() {
        val expr = "2+2"
        val res = "4"
        Assert.assertEquals(res, expressionCalculator(expr))

    }

    @Test
    fun testPlusTwo() {
        val expr = "5.1+0.1"
        val res = "5.2"
        Assert.assertEquals(res, expressionCalculator(expr))

    }
}