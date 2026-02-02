package com.pokedex.acceptance.drivers

import com.pokedex.domain.Pokemon

interface PokedexApisDriver {
    fun searchPokemon(name: String): Pokemon
}
