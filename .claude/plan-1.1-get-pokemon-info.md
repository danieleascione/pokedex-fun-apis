# Refactoring
- [ ] Improve deserialization in the PokedexApiServerSimulator and the PokedexApiServer and clearly separate the assertions (maybe they can share some code?)

# TODO
- [ ] FakePokemonRepository to be fully implemented (also unhappy path)
- [ ] FakePokemonRepository to run against contract tests
- [ ] Implement HttpPokemonRepository
- [ ] Dockerfile: Add multi-target stages to optionally skip JAR build (`--target with-build` vs `--target without-build`)
- [ ] Fix runtime warnings: add SLF4J provider dependency and `--enable-native-access=ALL-UNNAMED` JVM arg for Netty