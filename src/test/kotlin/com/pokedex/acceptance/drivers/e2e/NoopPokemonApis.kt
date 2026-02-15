package com.pokedex.acceptance.drivers.e2e

import com.pokedex.acceptance.drivers.PokemonApisDriver
import com.pokedex.domain.Pokemon

/**
 * Use this class when the Pokedex application is configured to rely on the public Pokemon APIs
 */
class NoopPokemonApis : PokemonApisDriver {
    override fun stores(pokemon: Pokemon) {
        // Nothing to do here
    }
}
