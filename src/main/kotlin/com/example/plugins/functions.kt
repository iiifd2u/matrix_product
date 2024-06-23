package com.example.plugins

import java.math.RoundingMode

val rowPattern = Regex("^\\[\\s*(\\-*\\d+\\.*\\d*\\s*)+\\]$")

typealias Matrix = List<List<Double>>

// Интерфейс ответа
sealed interface Result {
    class Success(val matrix: Matrix) : Result
    data object InvalidDifferentStrings : Result
    data object InvalidNotNumber : Result
    data object InvalidEmptyString : Result
}

fun parseStringToMatrix(inputString: String): Result {
    // Переводит строку в матрицу, возвращает пару Матрица-ошибка
    val res = mutableListOf<List<Double>>()
    inputString.split("\n").forEach {
        val row = it.replace("\\s+".toRegex(), " ").trim() //Убираем все лишние проблеы
        if (row == "[]") {
            return Result.InvalidEmptyString
        }
        val req = rowPattern.find(row)?.value
        if (!req.isNullOrBlank()) {

            val rowNumeric = row
                .substring(1, row.length - 1)
                .trim()
                .split("\\s+".toRegex())
                .map { v ->
                    v.toDouble()
                }
            res.add(rowNumeric)
        } else {
            return Result.InvalidNotNumber
        }
    }
    if (!res.all { it.count() == res[0].count() }) {
        return Result.InvalidDifferentStrings
    }
    return Result.Success(res)
}


fun matrixProduct(m1: Matrix, m2: Matrix): Matrix? {
    // считает произведение матриц
    val col1 = m1[0].count()
    val row2 = m2.count()
    if (col1 != row2) return null

    val col2 = m2[0].count()
    val row1 = m1.count()

    val res = mutableListOf<List<Double>>()
    for (i in 0 until row1) {
        val newRaw = mutableListOf<Double>()
        for (j in 0 until col2) {
            var sumEl = 0.0
            for (k in 0 until col1) {
                sumEl += m1[i][k] * m2[k][j]

            }
            newRaw.add(sumEl)
        }
        res.add(newRaw)
    }
    return res.toList()
        .map { row ->
            row
                .map { el ->
                    el
                        .toBigDecimal()
                        .setScale(3, RoundingMode.UP)
                        .toDouble()
                }
        }
}

