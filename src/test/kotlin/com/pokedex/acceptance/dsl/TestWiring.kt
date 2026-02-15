package com.pokedex.acceptance.dsl

import com.pokedex.acceptance.drivers.e2e.HttpPokedexApis
import com.pokedex.acceptance.drivers.e2e.NoopPokemonApis
import com.pokedex.acceptance.drivers.memory.LocalPokedexApisDriver

fun runAcceptanceTest(testScenario: Dsl.() -> Unit) {
    testScenario(testEnvironment().wireDsl())
}

private fun testEnvironment(): TestEnvironment = when (System.getProperty("TEST_ENVIRONMENT") ?: System.getenv("TEST_ENVIRONMENT")) {
    "E2E" -> TestEnvironment.EndToEnd
    /** Default to local unless specified otherwise. */
    else -> TestEnvironment.Component
}

sealed class TestEnvironment {
    abstract fun wireDsl(): Dsl

    /**
     * This is supposed to hit a production-like environment.
     * I'm taking a shortcut now and pretend the application will run in Docker in local.
     */
    data object EndToEnd : TestEnvironment() {
        override fun wireDsl(): Dsl = Dsl(NoopPokemonApis(), HttpPokedexApis())
    }

    /**
     * Hit the application without running the http server.
     */
    data object Component : TestEnvironment() {
        override fun wireDsl(): Dsl = Dsl(NoopPokemonApis(), LocalPokedexApisDriver())
    }
}