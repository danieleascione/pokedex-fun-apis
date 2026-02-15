package com.pokedex.outbound

import com.pokedex.domain.Pokemon
import com.pokedex.shell.outbound.FindByNameResult
import com.pokedex.shell.outbound.HttpPokemonRepository
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class HttpPokemonRepositoryTest {

    val pokemonRepository = HttpPokemonRepository()

    @Test
    fun `should find pokemon by name`() = runTest {
        // Given
        val existingPokemonName = "pikachu"

        // When
        val pokemon = pokemonRepository.findByName(existingPokemonName)

        // Then
        pokemon shouldBe FindByNameResult.Success(Pokemon(name = existingPokemonName))
    }
}
