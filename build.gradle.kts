plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
    application
}

application {
    mainClass.set("com.truelayer.MainKt")
}

group = "com.pokedex"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(25)
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.kotest.assertions.core)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}
