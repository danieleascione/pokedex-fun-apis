package com.pokedex.acceptance.dsl

import com.pokedex.acceptance.drivers.http.HttpPokedexApis
import com.pokedex.acceptance.drivers.http.NoopPokemonApis

fun runAcceptanceTest(type: TestEnvironment, testScenario: Dsl.() -> Unit) {
    testScenario(type.dsl())
}

sealed class TestEnvironment {
    abstract fun dsl(): Dsl

    /**
     * This is supposed to hit a production-like environment.
     * I'm taking a shortcut now and pretend the application will run in Docker in local.
     */
    data object EndToEnd : TestEnvironment() {
        override fun dsl(): Dsl = Dsl(NoopPokemonApis(), HttpPokedexApis())
    }
}
