package com.pokedex

import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test

class HealthCheckTest {
    @Test
    fun `health check works`() = testApplication {
        application { module() }
        client.get("/health").status shouldBe HttpStatusCode.OK
    }
}
