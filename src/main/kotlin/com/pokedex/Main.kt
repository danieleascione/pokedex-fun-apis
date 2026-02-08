package com.pokedex

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    routing {
        get("/health") {
            call.respondText("OK")
        }
        get("/pokemon/{name}") {
            val name = call.parameters["name"] ?: error("Missing pokemon name")
            call.respond(PokemonDTO(name = name))
        }
    }
}

@Serializable
class PokemonDTO(val name: String)
