package com.pokedex.shell.outbound

import com.pokedex.domain.Pokemon
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class HttpPokemonRepository(
    private val baseUrl: String = "https://pokeapi.co/api/v2",
) : PokemonRepository {

    private val client = HttpClient.newHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    override fun findByName(name: String): FindByNameResult {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/pokemon-species/$name"))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        val pokemonSpecies = json.decodeFromString<PokemonSpeciesResponse>(response.body())

        return FindByNameResult.Success(Pokemon(name = pokemonSpecies.name))
    }

    @Serializable
    private data class PokemonSpeciesResponse(val name: String)
}
