plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.7.2"
}

dependencies {
    paperweight.paperDevBundle("1.20-R0.1-SNAPSHOT")
    implementation(project(":common"))
}