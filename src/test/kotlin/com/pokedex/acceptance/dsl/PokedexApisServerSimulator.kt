package com.pokedex.acceptance.dsl

import com.pokedex.acceptance.drivers.PokedexApisDriver
import com.pokedex.domain.Pokemon
import com.pokedex.module
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class PokedexApisServerSimulator : PokedexApisDriver {

    override fun searchPokemon(name: String): Pokemon {
        var pokemon: Pokemon? = null
        testApplication {
            application {
                module()
            }
            client = createClient {
                this@testApplication.install(ContentNegotiation) { json() }
            }

            val response = client.get("/pokemon/$name")
            response.status shouldBe HttpStatusCode.OK
            val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
            val pokemonName = json["name"]?.jsonPrimitive?.content ?: error("Could not find pokemon name in response: $json")
            pokemon = Pokemon(name = pokemonName)
        }
        return pokemon ?: error("Pokemon with name $name not found")
    }
}
