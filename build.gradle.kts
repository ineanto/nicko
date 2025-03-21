plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.2"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.10"
}

group = "xyz.ineanto"
version = "1.2.0"

val invuiVersion: String = "1.44"

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

repositories {
    mavenCentral()
    mavenLocal()

    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://repo.xenondevs.xyz/releases") }
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { url = uri("https://repo.codemc.io/repository/maven-snapshots/") }
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")

    compileOnly("com.comphenix.protocol:ProtocolLib:5.4.0-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.5")
    compileOnly("net.kyori:adventure-api:4.17.0")
    compileOnly("xyz.xenondevs.invui:invui-core:$invuiVersion")

    implementation("com.github.jsixface:yamlconfig:1.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.18.1")
    implementation("com.mysql:mysql-connector-j:9.2.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.2")
    implementation("redis.clients:jedis:5.2.0")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks {

    processResources {
        from("src/main/resources")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        filesMatching("*.yml") {
            expand("version" to version)
        }
    }

    shadowJar {
        // RELOCATIONS
        relocate("com.github.jsixface", "xyz.ineanto.nicko.libs.yaml")
        relocate("me.clip", "xyz.ineanto.nicko.libs.placeholderapi")
        relocate("com.fasterxml.jackson", "xyz.ineanto.nicko.libs.jacksonpr")
        relocate("com.mysql", "xyz.ineanto.nicko.libs.mysql")
        relocate("org.mariadb.jdbc", "xyz.ineanto.nicko.libs.mariadb")
        relocate("redis.clients", "xyz.ineanto.nicko.libs.redis")
        relocate("com.google.gson", "xyz.ineanto.nicko.libs.gson")
        relocate("org.apache.commons.pool2", "xyz.ineanto.nicko.libs.pool2")
        relocate("org.bstats", "xyz.ineanto.nicko.libs.bstats")

        // EXCLUSIONS
        exclude("colors.bin")
        exclude("waffle/**")
        exclude("com/sun/**")
        exclude("com/google/protobuf/**")
        exclude("com/google/errorprone/**")
        exclude("org/apache/commons/logging/**")
        exclude("org/jetbrains/**")
        exclude("org/intellij/**")
        exclude("org/checkerframework/**")
        exclude("org/json/**")
        exclude("org/slf4j/**")
        exclude("org/yaml/**")
        exclude("google/protobuf/**")
        exclude("net/kyori/**")

        // MINIFY
        minimize {
            exclude(dependency("org.bstats:.*"))
        }

        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }

    runServer {
        downloadPlugins {
            url("https://download.luckperms.net/1568/bukkit/loader/LuckPerms-Bukkit-5.4.151.jar")

            // 1.20.5 - latest testing
            url("https://ci.dmulloy2.net/job/ProtocolLib/lastSuccessfulBuild/artifact/build/libs/ProtocolLib.jar")
        }

        minecraftVersion("1.21.4")
    }
}