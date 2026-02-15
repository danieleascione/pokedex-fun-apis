package com.pokedex.shell.outbound

import com.pokedex.domain.Pokemon
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class HttpPokemonRepository(
    private val baseUrl: String = "https://pokeapi.co/api/v2",
) : PokemonRepository {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    override suspend fun findByName(name: String): FindByNameResult {
        val pokemonSpecies = client.get("$baseUrl/pokemon-species/$name").body<PokemonSpeciesResponse>()

        return FindByNameResult.Success(Pokemon(name = pokemonSpecies.name))
    }

    @Serializable
    private data class PokemonSpeciesResponse(val name: String)
}
