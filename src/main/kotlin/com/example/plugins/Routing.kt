package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*

data class MatrixString(val value: String)

fun Application.configureRouting() {
    install(ContentNegotiation) {
        gson()
    }
    routing {
        staticResources(remotePath = "/", basePackage = "static", index = "index.html")

        post("/calculate") {
            val stringFromJson = call.receive<MatrixString>().value

            if (stringFromJson.length <= 5) {
                val err = "Введите обе матрицы!"
                call.respondText(err, contentType = ContentType.Text.Plain, status = HttpStatusCode(418, "math error"))
            } else {
                val matrixPair = stringFromJson.split("|")
                val matrixLeft = matrixPair[0]
                val matrixRight = matrixPair[1]
                if (matrixLeft.length < 3 || matrixRight.length < 3) {
                    val err = "Одна из матриц пуста"
                    call.respondText(
                        err,
                        contentType = ContentType.Text.Plain,
                        status = HttpStatusCode(418, "math error")
                    )
                } else {

                    var m1: Matrix? = null
                    var m2: Matrix? = null
                    when (
                        val resLeft = parseStringToMatrix(matrixLeft)
                    ) {
                        is Result.Success -> {
                            m1 = resLeft.matrix
                        }

                        Result.InvalidNotNumber -> call.respondText(
                            "В левой матрице должны быть только числа!",
                            contentType = ContentType.Text.Plain,
                            status = HttpStatusCode(418, "schema error")
                        )

                        Result.InvalidEmptyString -> call.respondText(
                            "В левой матрице не должно быть пустых строк!",
                            contentType = ContentType.Text.Plain,
                            status = HttpStatusCode(418, "schema error")
                        )

                        Result.InvalidDifferentStrings -> call.respondText(
                            "В левой матрице не должно быть строк разной длины!",
                            contentType = ContentType.Text.Plain,
                            status = HttpStatusCode(418, "schema error")
                        )
                    }

                    when (
                        val resRight = parseStringToMatrix(matrixRight)
                    ) {
                        is Result.Success -> {
                            m2 = resRight.matrix
                        }

                        Result.InvalidNotNumber -> call.respondText(
                            "В правой матрице должны быть только числа!",
                            contentType = ContentType.Text.Plain,
                            status = HttpStatusCode(418, "schema error")
                        )

                        Result.InvalidEmptyString -> call.respondText(
                            "В правой матрице не должно быть пустых строк!",
                            contentType = ContentType.Text.Plain,
                            status = HttpStatusCode(418, "schema error")
                        )

                        Result.InvalidDifferentStrings -> call.respondText(
                            "В правой матрице не должно быть строк разной длины!",
                            contentType = ContentType.Text.Plain,
                            status = HttpStatusCode(418, "schema error")
                        )
                    }

                    if (m1.isNullOrEmpty() || m2.isNullOrEmpty()) {
                        val err = "Одна из матриц не заполнена"
                        call.respondText(
                            err,
                            contentType = ContentType.Text.Plain,
                            status = HttpStatusCode(418, "math error")
                        )
                    } else {
                        val matProd = matrixProduct(m1, m2)
                        if (matProd.isNullOrEmpty()) {
                            val err = "Число столбцов первой матрицы должно равняться числу строк второй!"
                            call.respondText(
                                err,
                                contentType = ContentType.Text.Plain,
                                status = HttpStatusCode(418, "math error")
                            )
                        } else {
                            call.respond<Matrix>(matProd)
                        }
                    }
                }
            }
        }
    }
}


