plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.2"
    id("xyz.jpenilla.run-paper") version "2.2.2"
}

group = "xyz.ineanto"
version = "1.1.6-RC1"

val shadowImplementation: Configuration by configurations.creating
configurations["implementation"].extendsFrom(shadowImplementation)
configurations["testImplementation"].extendsFrom(shadowImplementation)

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        name = "dmulloy2"
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }
    maven {
        name = "xenondevs"
        url = uri("https://repo.xenondevs.xyz/releases")
    }
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "codemc"
        url = uri("https://repo.codemc.io/repository/maven-snapshots/")
    }
    maven {
        name = "placeholderapi"
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

dependencies {
    implementation("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation("com.comphenix.protocol:ProtocolLib:5.2.0-SNAPSHOT")

    shadowImplementation("me.clip:placeholderapi:2.11.5")
    shadowImplementation("net.kyori:adventure-api:4.14.0")
    shadowImplementation("xyz.xenondevs.invui:invui:1.30")
    shadowImplementation("net.wesjd:anvilgui:1.9.3-SNAPSHOT")
    shadowImplementation("com.github.jsixface:yamlconfig:1.2")
    shadowImplementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")
    shadowImplementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    shadowImplementation("com.mysql:mysql-connector-j:8.2.0")
    shadowImplementation("org.mariadb.jdbc:mariadb-java-client:3.3.1")
    shadowImplementation("redis.clients:jedis:5.1.2")
    shadowImplementation("com.google.code.gson:gson:2.10.1")
    shadowImplementation("org.bstats:bstats-bukkit:3.0.2")

    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.86.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
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
        configurations = listOf(shadowImplementation)

        // NAMING
        archiveBaseName.set("nicko")
        archiveVersion.set(version.toString())
        archiveAppendix.set("")
        archiveClassifier.set("")

        // RELOCATIONS
        relocate("xyz.xenondevs", "xyz.ineanto.nicko.libs.invui")
        relocate("me.clip", "xyz.ineanto.nicko.libs.placeholderapi")
        relocate("net.wesjd", "xyz.ineanto.nicko.libs.anvilgui")
        relocate("com.github.jsixface", "xyz.ineanto.nicko.libs.yaml")
        relocate("com.fasterxml.jackson.dataformat", "xyz.ineanto.nicko.libs.jackson.yaml")
        relocate("com.fasterxml.jackson.core", "xyz.ineanto.nicko.libs.jackson.core")
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
            exclude(dependency("xyz.xenondevs.invui:.*"))
            exclude(dependency("net.wesjd:.*"))
            exclude(dependency("org.bstats:.*"))
        }
    }

    jar {
        enabled = false
    }

    test {
        useJUnitPlatform()
    }

    runServer {
        dependsOn(shadowJar)

        downloadPlugins {
            url("https://download.luckperms.net/1539/bukkit/loader/LuckPerms-Bukkit-5.4.126.jar")
            url("https://ci.dmulloy2.net/job/ProtocolLib/lastSuccessfulBuild/artifact/build/libs/ProtocolLib.jar")
        }

        minecraftVersion("1.20.4")
    }
}