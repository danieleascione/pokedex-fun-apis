package com.pokedex.acceptance.drivers

import com.pokedex.domain.Pokemon

/**
 * Driver for the external Pokemon API that provides Pokemon data.
 * Used to set up the required preconditions before exercising the Pokedex API.
 *
 * Actor -> Pokedex Api -> **Pokemon Apis**
 */
interface PokemonApisDriver {
    fun stores(pokemon: Pokemon): Pokemon = pokemon
}
