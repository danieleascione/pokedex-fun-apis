package com.pokedex.shell.outbound

import com.pokedex.domain.Pokemon
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class HttpPokemonRepository : PokemonRepository {

    private val client = HttpClient.newHttpClient()

    override fun findByName(name: String): FindByNameResult {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://pokeapi.co/api/v2/pokemon-species/$name"))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        val body = Json.parseToJsonElement(response.body()).jsonObject
        val pokemonName = body["name"]!!.jsonPrimitive.content

        return FindByNameResult.Success(Pokemon(name = pokemonName))
    }
}
