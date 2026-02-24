package com.example.calculator.domain.model

data class CalculatorState(
    val input: String = "",
    val result: String = "0",
    val isResult: Boolean = false
)