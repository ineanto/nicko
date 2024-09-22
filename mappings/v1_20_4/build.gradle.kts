plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.7.2"
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
    compileOnly(project(":mappings"))
}