package com.pokedex.acceptance.drivers.memory

import com.pokedex.acceptance.drivers.PokemonApisDriver
import com.pokedex.acceptance.fakes.FakePokemonRepository
import com.pokedex.domain.Pokemon

class InMemoryPokemonApis(private val fakePokemonRepository: FakePokemonRepository) : PokemonApisDriver {

    override fun stores(pokemon: Pokemon) = fakePokemonRepository.stores(pokemon)
}
