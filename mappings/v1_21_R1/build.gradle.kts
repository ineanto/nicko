plugins {
    id("java")
}

group = "xyz.ineanto.nicko.mappings"
version = project.version

// maybe see
// https://github.com/tagavari/nms-remap?tab=readme-ov-file


repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly(project(":mappings"))
    compileOnly(project(":common"))
    compileOnly("org.spigotmc:spigot:1.21.3-R0.1-SNAPSHOT")
}