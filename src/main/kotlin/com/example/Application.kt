package com.example.com.example

import io.ktor.server.routing.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8080) {
        routing {
            staticFiles("/", File("static"), index="index.html")
        }

    }.start(wait = true)
}