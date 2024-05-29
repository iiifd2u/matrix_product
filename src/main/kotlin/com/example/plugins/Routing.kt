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
                    val matrixLeftPair = parseStringToMatrix(matrixLeft)
                    val matrixRightPair = parseStringToMatrix(matrixRight)
                    val m1 = matrixLeftPair.first
                    val m2 = matrixRightPair.first
                    val errorL = matrixLeftPair.second
                    val errorR = matrixRightPair.second

                    if (errorL.isNotEmpty()) {
                        call.respondText(
                            errorL,
                            contentType = ContentType.Text.Plain,
                            status = HttpStatusCode(418, "parse error")
                        )
                    }

                    if (errorR.isNotEmpty()) {
                        call.respondText(
                            errorR,
                            contentType = ContentType.Text.Plain,
                            status = HttpStatusCode(418, "parse error")
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


