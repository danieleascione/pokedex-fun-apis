package com.pokedex.acceptance.dsl

import com.pokedex.acceptance.drivers.PokedexApisDriver
import com.pokedex.acceptance.drivers.PokemonApisDriver
import com.pokedex.domain.Pokemon

/**
 * This main DSL should provide domain based language access for the tests.
 * Normally it would delegate to the specific DLS classes, rather than the drivers directly.
 * That level of indirection allows us to swap the names for aliases
 *
 * At this stage I do not need aliases, so this is fine.
 */
class Dsl(val pokemonApis: PokemonApisDriver, val pokedexApis: PokedexApisDriver)

fun pokemon(name: String) = Pokemon(name)
