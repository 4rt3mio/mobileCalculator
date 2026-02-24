package com.example.calculator.domain.usecase

import com.example.calculator.domain.model.CalculatorState
import com.example.calculator.domain.model.Operation
import com.example.calculator.domain.model.HistoryItem
import com.example.calculator.domain.repository.CalculatorRepository

class CalculatorUseCase(
    private val repository: CalculatorRepository? = null
) {

    fun processAction(state: CalculatorState, action: String): CalculatorState {
        val operation = Operation.fromSymbol(action)
        return if (operation != null) {
            handleOperation(state, operation)
        } else {
            handleDigit(state, action)
        }
    }

    private fun handleOperation(state: CalculatorState, op: Operation): CalculatorState {
        return when (op) {
            Operation.CLEAR -> CalculatorState()
            Operation.EQUALS -> evaluate(state)
            Operation.ADD, Operation.SUBTRACT, Operation.MULTIPLY, Operation.DIVIDE ->
                handleOperator(state, op.symbol)
            Operation.DOT -> handleDot(state)
            Operation.PERCENT, Operation.SIGN -> state
        }
    }

    private fun evaluate(state: CalculatorState): CalculatorState {
        if (state.input.isEmpty()) return state
        val expression = state.input.replace(',', '.')
        val result = try {
            evaluateExpression(expression)
        } catch (e: Exception) {
            null
        }
        return if (result == null) {
            state.copy(result = "Error", isResult = true)
        } else {
            val formatted = formatResult(result)
            // TODO: сохранить в историю через repository
            state.copy(input = "", result = formatted, isResult = true)
        }
    }

    private fun handleOperator(state: CalculatorState, operator: String): CalculatorState {
        val newInput = if (state.isResult) {
            state.result + operator
        } else {
            if (state.input.isNotEmpty() && state.input.last() in listOf('+', '-', '*', '/')) {
                state.input.dropLast(1) + operator
            } else {
                state.input + operator
            }
        }
        return state.copy(input = newInput, isResult = false)
    }

    private fun handleDigit(state: CalculatorState, digit: String): CalculatorState {
        val newInput = if (state.isResult) {
            digit
        } else {
            state.input + digit
        }
        return state.copy(input = newInput, isResult = false)
    }

    private fun handleDot(state: CalculatorState): CalculatorState {
        if (state.isResult) {
            return state.copy(input = "0.", isResult = false)
        }
        val expression = state.input
        val lastOperatorIndex = expression.indexOfLast { it in listOf('+', '-', '*', '/') }
        val lastNumber = if (lastOperatorIndex == -1) expression else expression.substring(lastOperatorIndex + 1)

        if (lastNumber.contains('.')) return state
        if (lastNumber.isEmpty()) {
            return state.copy(input = expression + "0.", isResult = false)
        }
        return state.copy(input = expression + ".", isResult = false)
    }

    private fun evaluateExpression(expr: String): Double? {
        val operators = listOf('+', '-', '*', '/')
        val op = expr.find { it in operators } ?: return null
        val parts = expr.split(op)
        if (parts.size != 2) return null
        val a = parts[0].toDoubleOrNull() ?: return null
        val b = parts[1].toDoubleOrNull() ?: return null
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> if (b != 0.0) a / b else null
            else -> null
        }
    }

    private fun formatResult(value: Double): String {
        return if (value == value.toLong().toDouble()) {
            value.toLong().toString()
        } else {
            String.format("%.10f", value).trimEnd('0').trimEnd('.')
        }.replace('.', ',')
    }
}