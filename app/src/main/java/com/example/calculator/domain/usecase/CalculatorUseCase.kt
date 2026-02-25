package com.example.calculator.domain.usecase

import com.example.calculator.domain.model.CalculatorState
import com.example.calculator.domain.model.Operation
import java.util.Stack
import kotlin.math.*

class CalculatorUseCase {

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
            Operation.BACKSPACE -> handleBackspace(state)
            Operation.PERCENT -> handlePercent(state)
            Operation.SIGN -> handleSign(state)
            Operation.SQUARE -> handleSquare(state)
            Operation.SQRT -> handleSqrt(state)
            Operation.ONE_OVER -> handleOneOver(state)
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
        if (state.isResult) {
            return state.copy(input = digit, isResult = false)
        }
        if (state.input.length >= 12) return state
        val newInput = state.input + digit
        return state.copy(input = newInput, isResult = false)
    }

    private fun handleBackspace(state: CalculatorState): CalculatorState {
        if (state.isResult) return CalculatorState()
        val newInput = state.input.dropLast(1)
        return state.copy(input = newInput)
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

    private fun handlePercent(state: CalculatorState): CalculatorState {
        val (newInput, newResult) = applyUnary(state) { it / 100.0 }
        return if (newResult == null) state else state.copy(input = newInput, result = newResult, isResult = false)
    }

    private fun handleSign(state: CalculatorState): CalculatorState {
        val (newInput, newResult) = applyUnary(state) { -it }
        return if (newResult == null) state else state.copy(input = newInput, result = newResult, isResult = false)
    }

    private fun handleSquare(state: CalculatorState): CalculatorState {
        val (newInput, newResult) = applyUnary(state) { it * it }
        return if (newResult == null) state else state.copy(input = newInput, result = newResult, isResult = false)
    }

    private fun handleSqrt(state: CalculatorState): CalculatorState {
        val (newInput, newResult) = applyUnary(state) {
            if (it >= 0) sqrt(it) else null
        }
        return if (newResult == null) state.copy(result = "Error", isResult = true) else state.copy(input = newInput, result = newResult, isResult = false)
    }

    private fun handleOneOver(state: CalculatorState): CalculatorState {
        val (newInput, newResult) = applyUnary(state) {
            if (it != 0.0) 1.0 / it else null
        }
        return if (newResult == null) state.copy(result = "Error", isResult = true) else state.copy(input = newInput, result = newResult, isResult = false)
    }

    private fun applyUnary(state: CalculatorState, operation: (Double) -> Double?): Pair<String, String?> {
        val expr = if (state.isResult) state.result else state.input
        if (expr.isEmpty()) return Pair("", null)
        val lastNumber = extractLastNumber(expr) ?: return Pair(expr, null)
        val number = lastNumber.toDoubleOrNull() ?: return Pair(expr, null)
        val result = operation(number) ?: return Pair(expr, null)
        val formattedResult = formatResult(result)
        val newExpr = replaceLast(expr, lastNumber, formattedResult)
        return Pair(newExpr, formattedResult)
    }

    private fun extractLastNumber(expr: String): String? {
        val operators = listOf('+', '-', '*', '/')
        val lastOpIndex = expr.indexOfLast { it in operators }
        return if (lastOpIndex == -1) expr else expr.substring(lastOpIndex + 1)
    }

    private fun replaceLast(original: String, old: String, new: String): String {
        val lastIndex = original.lastIndexOf(old)
        return if (lastIndex >= 0) {
            original.substring(0, lastIndex) + new
        } else {
            original
        }
    }

    private fun evaluate(state: CalculatorState): CalculatorState {
        if (state.input.isEmpty()) return state
        val result = evaluateExpression(state.input)
        return if (result == null) {
            state.copy(result = "Error", isResult = true)
        } else {
            val formatted = formatResult(result)
            state.copy(input = "", result = formatted, isResult = true)
        }
    }

    private fun evaluateExpression(expr: String): Double? {
        val tokens = tokenize(expr)
        val postfix = infixToPostfix(tokens)
        return evaluatePostfix(postfix)
    }

    private fun tokenize(expr: String): List<String> {
        val result = mutableListOf<String>()
        var i = 0
        val n = expr.length
        while (i < n) {
            val c = expr[i]
            when {
                c.isWhitespace() -> i++
                c in listOf('+', '-', '*', '/') -> {
                    result.add(c.toString())
                    i++
                }
                c.isDigit() || c == '.' -> {
                    val start = i
                    i++
                    while (i < n && (expr[i].isDigit() || expr[i] == '.')) {
                        i++
                    }
                    result.add(expr.substring(start, i))
                }
                else -> i++
            }
        }
        return result
    }

    private fun infixToPostfix(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val stack = Stack<String>()
        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> output.add(token)
                token in listOf("+", "-", "*", "/") -> {
                    while (stack.isNotEmpty() && stack.peek() in listOf("+", "-", "*", "/") && precedence(stack.peek()) >= precedence(token)) {
                        output.add(stack.pop())
                    }
                    stack.push(token)
                }
            }
        }
        while (stack.isNotEmpty()) {
            output.add(stack.pop())
        }
        return output
    }

    private fun precedence(op: String): Int {
        return when (op) {
            "+", "-" -> 1
            "*", "/" -> 2
            else -> 0
        }
    }

    private fun evaluatePostfix(tokens: List<String>): Double? {
        val stack = Stack<Double>()
        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> stack.push(token.toDouble())
                token in listOf("+", "-", "*", "/") -> {
                    if (stack.size < 2) return null
                    val b = stack.pop()
                    val a = stack.pop()
                    val result = when (token) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> if (b != 0.0) a / b else return null
                        else -> return null
                    }
                    stack.push(result)
                }
            }
        }
        return if (stack.size == 1) stack.pop() else null
    }

    private fun formatResult(value: Double): String {
        val str = if (value == value.toLong().toDouble()) {
            value.toLong().toString()
        } else {
            value.toString()
        }
        return if (str.length > 12) str.take(12) else str
    }
}