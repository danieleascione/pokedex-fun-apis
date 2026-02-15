package com.pokedex.shell.outbound

import com.pokedex.domain.Pokemon

interface PokemonRepository {

    fun findByName(name: String): FindByNameResult
}

sealed class FindByNameResult {
    data class Success(val value: Pokemon) : FindByNameResult()
    object Failure : FindByNameResult()
}