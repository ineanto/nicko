plugins {
    id("java")
}

group = "xyz.ineanto.nicko"
version = project.version

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.23.3-R0.1-SNAPSHOT")
}