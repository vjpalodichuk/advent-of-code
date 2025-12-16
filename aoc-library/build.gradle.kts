plugins {
    id("com.capital7software.library-conventions")
}

version = rootProject.version

tasks.named("compileJava", JavaCompile::class.java) {
  options.compilerArgumentProviders.add(CommandLineArgumentProvider {
    // Provide compiled Kotlin classes to javac – needed for Java/Kotlin mixed sources to work
    listOf("--patch-module", "com.capital7software.aoc.lib=${sourceSets["main"].output.asPath}")
  })
}
