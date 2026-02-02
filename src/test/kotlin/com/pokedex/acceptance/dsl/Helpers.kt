package com.pokedex.acceptance.dsl

import com.pokedex.domain.Pokemon
import io.kotest.matchers.shouldBe

fun Pokemon.shouldHave(name: String) = this.name shouldBe name
