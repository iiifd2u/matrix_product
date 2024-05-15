package com.example.plugins

import java.math.RoundingMode

val rowPattern = Regex("^\\[\\s*(\\-*\\d+\\.*\\d*\\s*)+\\]$")

typealias Matrix = List<List<Double>>


fun parseStringToMatrix(inputString: String):Matrix?{
    // Переводит строку в матрицу, делает проверку по равно длине строк и по валидности данных в строке
    val res = mutableListOf<List<Double>>()
    inputString.split("\n").forEach {
        val row = it.replace("\\s+".toRegex(), " ").trim()
        if (!rowPattern.find(row)?.value.isNullOrBlank()){

            val rowNumeric = row
                .substring(1, row.length-1)
                .trim()
                .split("\\s+".toRegex())
                .map {v->
                    v.toDouble()
                }
            res.add(rowNumeric)
        } else{
            println("Ошибка при вводе матрицы")
            return null
        }
    }
    if (!res.all {it.count() == res[0].count()}){
        return null
    }
    return res
}


fun matrixProduct(m1:Matrix, m2:Matrix):Matrix?{
    val col_1 = m1[0].count()
    val row_2 = m2.count()
    if (col_1!=row_2) return null

    val col_2 = m2[0].count()
    val row_1 = m1.count()

    val res = mutableListOf<List<Double>>()
    for (i in 0 until row_1){
        val newRaw = mutableListOf<Double>()
        for (j in 0 until col_2){
            var sumEl = 0.0
            for (k in 0 until col_1){
                sumEl+=m1[i][k]*m2[k][j]

            }
            newRaw.add(sumEl)
        }
        res.add(newRaw)
    }
    return res.toList()
        .map { row->row
            .map { el->el
                .toBigDecimal()
                .setScale(3, RoundingMode.UP)
                .toDouble() } }
}
