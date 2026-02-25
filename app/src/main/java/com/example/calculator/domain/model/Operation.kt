package com.example.calculator.domain.model

enum class Operation(val symbol: String) {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    PERCENT("%"),
    SIGN("±"),
    CLEAR("C"),
    EQUALS("="),
    DOT("."),
    BACKSPACE("⌫"),
    SQUARE("x²"),
    SQRT("√"),
    ONE_OVER("1/x");

    companion object {
        fun fromSymbol(symbol: String): Operation? {
            return values().find { it.symbol == symbol }
        }
    }
}