package com.pokedex.shell

import com.pokedex.domain.Pokemon
import com.pokedex.shell.outbound.FindByNameResult
import com.pokedex.shell.outbound.PokemonRepository
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable

fun Route.getPokemonByName(pokemonRepository: PokemonRepository) = get("/pokemon/{name}") {
    val name = call.parameters["name"]!!

    val result = when (val pokemonSearchResult = pokemonRepository.findByName(name)) {
        is FindByNameResult.Success -> pokemonSearchResult.value.toHttpDto()
        is FindByNameResult.Failure -> TODO()
    }

    call.respond(result)
}

private fun Pokemon.toHttpDto() = PokemonDTO(name)

@Serializable
class PokemonDTO(val name: String)
