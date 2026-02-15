package com.pokedex

import com.pokedex.shell.getPokemonByName
import com.pokedex.shell.healthCheck
import com.pokedex.shell.outbound.HttpPokemonRepository
import com.pokedex.shell.outbound.PokemonRepository
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(Netty, port = 8080) {
        module()
    }.start(wait = true)
}

/**
 * Using [the parameterless instantiation pattern](https://www.jamesshore.com/v2/projects/nullables/testing-without-mocks#instantiation):
 * - Sensible defaults for the production code so that the tests don't require a different setup unless they want to override the specific behaviour.
 */
fun Application.module(pokemonRepository: PokemonRepository = HttpPokemonRepository()) {
    install(ContentNegotiation) { json() }
    routing {
        healthCheck()
        getPokemonByName(pokemonRepository)
    }
}
