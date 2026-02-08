package com.pokedex.acceptance.dsl

import com.pokedex.acceptance.drivers.http.HttpPokedexApis
import com.pokedex.acceptance.drivers.http.NoopPokemonApis

fun runAcceptanceTest(testScenario: Dsl.() -> Unit) {
    testScenario(testEnvironment().dsl())
}

private fun testEnvironment(): TestEnvironment = when (System.getProperty("TEST_ENVIRONMENT") ?: System.getenv("TEST_ENVIRONMENT")) {
    "E2E" -> TestEnvironment.EndToEnd
    /** Default to local unless specified otherwise. */
    else -> TestEnvironment.LocalServer
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

    /**
     * Hit the application without running the http server.
     */
    data object LocalServer : TestEnvironment() {
        override fun dsl(): Dsl = Dsl(NoopPokemonApis(), PokedexApisServerSimulator())
    }
}
