package com.example.calculator
import android.util.Log
import com.fathzer.soft.javaluator.DoubleEvaluator
import java.lang.Math.floor

/**
 *  Calculating the result for the [expression]
*/

fun expressionCalculator(expression: String): String {
    try{
        val result: Double = DoubleEvaluator().evaluate(expression)
        return if (floor(result) == result) {
            result.toInt().toString()
        } else {
            result.toString()
        }
    } catch (e: Exception) {
        return expression
    }

}

fun expressionCalculatorDouble(expression: String): Double {
    try{
        val result: Double = DoubleEvaluator().evaluate(expression)
        return if (floor(result) == result) {
            result
        } else {
            result
        }
    } catch (e: Exception) {
        return 0.0
    }

}