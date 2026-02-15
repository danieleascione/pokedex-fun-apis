package com.pokedex.acceptance.drivers.memory

import com.pokedex.acceptance.drivers.PokedexApisDriver
import com.pokedex.domain.Pokemon
import com.pokedex.module
import com.pokedex.shell.outbound.PokemonRepository
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class LocalPokedexApisDriver(private val pokemonRepository: PokemonRepository) : PokedexApisDriver {

    override fun searchPokemon(name: String): Pokemon {
        var pokemon: Pokemon? = null
        testApplication {
            application {
                module(pokemonRepository)
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
