plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.7.2"
}

dependencies {
    paperweight.paperDevBundle("1.20-R0.1-SNAPSHOT")
    compileOnly(project(":mappings"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}