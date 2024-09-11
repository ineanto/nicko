plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allprojects {
    group = "xyz.ineanto.nicko"
    version = "1.2.0"
}