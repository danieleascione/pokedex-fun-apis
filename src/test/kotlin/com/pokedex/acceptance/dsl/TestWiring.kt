package com.pokedex.acceptance.dsl

import com.pokedex.acceptance.drivers.http.HttpPokedexApis
import com.pokedex.acceptance.drivers.http.HttpPokemonApis

fun runAcceptanceTest(type: TestEnvironment, testScenario: Dsl.() -> Unit) {
    val dsl = when (type) {
        // Pokedex APIs run locally
        TestEnvironment.E2E -> Dsl(HttpPokemonApis(), HttpPokedexApis())
    }
    testScenario(dsl)
}

enum class TestEnvironment {
    /**
     * This is supposed to hit a production-like environment.
     * I'm taking a shortcut now and pretend the application will run in Docker in local.
     */
    E2E,
}
