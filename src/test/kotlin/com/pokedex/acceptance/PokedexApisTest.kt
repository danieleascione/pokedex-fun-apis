package com.pokedex.acceptance

import com.pokedex.acceptance.dsl.pokemon
import com.pokedex.acceptance.dsl.runAcceptanceTest
import com.pokedex.acceptance.dsl.shouldHave
import org.junit.jupiter.api.Test

/**
 * Story 1.1: Retrieve Basic Pokemon by Name
 *
 * As an API consumer
 * I want to request Pokemon information by name
 * So that I can get essential details without parsing complex PokéAPI responses
 */
class PokedexApisTest {

    /**
     * This test for now runs only if the application is running.
     */
    @Test
    fun `returns pokemon information by name`() = runAcceptanceTest {
        pokemonApis.stores(pokemon(name = "pikachu"))

        val pokemonInfo = pokedexApis.searchPokemon("pikachu")

        pokemonInfo.shouldHave(name = "pikachu")
    }
}
