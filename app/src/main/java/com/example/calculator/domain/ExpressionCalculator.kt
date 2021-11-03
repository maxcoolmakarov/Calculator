package com.example.calculator
import android.util.Log
import com.fathzer.soft.javaluator.DoubleEvaluator
import java.lang.Math.floor

/**
 *  Calculating the result for the [expression]
*/

fun expressionCalculator(expression: String, format: Int = 0): String {
    Log.d("calculating", format.toString())
    try{
        val result: Double = DoubleEvaluator().evaluate(expression)
        return if (floor(result) == result) {
            result.toInt().toString()
        } else {
            if (format > 0) {
                result.format(format)
            }
            else
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

fun Double.format(digits: Int) = "%.${digits}f".format(this)