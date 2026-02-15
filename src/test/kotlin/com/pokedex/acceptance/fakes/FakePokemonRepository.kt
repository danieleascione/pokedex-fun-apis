package com.pokedex.acceptance.fakes

import com.pokedex.domain.Pokemon
import com.pokedex.shell.outbound.FindByNameResult
import com.pokedex.shell.outbound.PokemonRepository

class FakePokemonRepository : PokemonRepository {

    private val storedPokemon = mutableMapOf<String, Pokemon>()

    fun stores(pokemon: Pokemon) {
        storedPokemon[pokemon.name] = pokemon
    }

    override fun findByName(name: String): FindByNameResult {
        val pokemon = storedPokemon[name]
        return FindByNameResult.Success(pokemon!!)
    }
}
