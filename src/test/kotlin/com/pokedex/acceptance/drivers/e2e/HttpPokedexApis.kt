package com.pokedex.acceptance.drivers.e2e

import com.pokedex.acceptance.drivers.PokedexApisDriver
import com.pokedex.domain.Pokemon
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class HttpPokedexApis(private val pokedexApisUrl: String = "http://localhost:8080") : PokedexApisDriver {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) { json() }
    }

    override fun searchPokemon(name: String): Pokemon = runBlocking {
        val pokemonEndpoint = "$pokedexApisUrl/pokemon/$name"
        val response = client.get(pokemonEndpoint)
        withClue("Pokedex APIs contacted at: $pokemonEndpoint didn't succeed") {
            response.status shouldBe HttpStatusCode.OK
        }

        val body = response.bodyAsText()
        val json = Json.parseToJsonElement(body).jsonObject
        val name = json["name"]?.jsonPrimitive?.content ?: error("Error in deserializing pokemon with $name")
        Pokemon(name = name)
    }
}