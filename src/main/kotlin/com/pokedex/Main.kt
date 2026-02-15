package com.pokedex

import com.pokedex.shell.getPokemonByName
import com.pokedex.shell.healthCheck
import com.pokedex.shell.outbound.FindByNameResult
import com.pokedex.shell.outbound.PokemonRepository
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(Netty, port = 8080) {
        module(failingPokemonRepo)
    }.start(wait = true)
}

fun Application.module(pokemonRepository: PokemonRepository = failingPokemonRepo) {
    routing {
        healthCheck()
        getPokemonByName(pokemonRepository)
    }
}

val failingPokemonRepo = object : PokemonRepository {
    override fun findByName(name: String): FindByNameResult {
        TODO("Not yet implemented")
    }
}