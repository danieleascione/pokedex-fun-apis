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
    /**
     * This is supposed to hit a production-like environment.
     * I'm taking a shortcut now and pretend the application will run in Docker in local.
     */
    E2E,
}
