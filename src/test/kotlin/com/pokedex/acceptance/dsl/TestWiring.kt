package com.pokedex.acceptance.dsl

import com.pokedex.acceptance.drivers.http.HttpPokedexApis
import com.pokedex.acceptance.drivers.http.HttpPokemonApis

fun runAcceptanceTest(type: Type, testScenario: Dsl.() -> Unit) {
    val dsl = when (type) {
        // Pokedex APIs run locally
        Type.E2E -> Dsl(HttpPokemonApis(), HttpPokedexApis())
    }
    testScenario(dsl)
}

enum class Type {
    E2E,
}
