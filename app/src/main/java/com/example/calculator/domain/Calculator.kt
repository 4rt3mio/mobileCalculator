package com.example.calculator.domain

class Calculator {
    fun evaluate(expression: String): String {
        return try {
            val operators = listOf('+', '-', '*', '/')
            val op = expression.find { it in operators } ?: return "Error"
            val parts = expression.split(op)
            if (parts.size != 2) return "Error"
            val a = parts[0].toDoubleOrNull() ?: return "Error"
            val b = parts[1].toDoubleOrNull() ?: return "Error"
            val result = when (op) {
                '+' -> a + b
                '-' -> a - b
                '*' -> a * b
                '/' -> if (b != 0.0) a / b else return "Error"
                else -> return "Error"
            }
            if (result == result.toLong().toDouble()) result.toLong().toString()
            else result.toString()
        } catch (e: Exception) {
            "Error"
        }
    }
}