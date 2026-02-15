package com.pokedex.shell

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable

fun Route.getPokemonByName() = get("/pokemon/{name}") {
    val name = call.parameters["name"] ?: error("Missing pokemon name")
    call.respond(PokemonDTO(name = name))
}

@Serializable
class PokemonDTO(val name: String)