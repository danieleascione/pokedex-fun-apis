package com.pokedex.acceptance.drivers

import com.pokedex.domain.Pokemon

interface PokemonApisDriver {
    fun stores(pokemon: Pokemon): Pokemon = pokemon
}
