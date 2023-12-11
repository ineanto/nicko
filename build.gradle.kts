import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("io.github.goooler.shadow") version "8.1.2"
    id("java")
}

group = "xyz.ineanto"
version = "1.0.5-RC1"

val shadowImplementation: Configuration by configurations.creating
configurations["implementation"].extendsFrom(shadowImplementation)
configurations["testImplementation"].extendsFrom(shadowImplementation)

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
    implementation("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    implementation("com.comphenix.protocol:ProtocolLib:5.1.1-SNAPSHOT")

    shadowImplementation("me.clip:placeholderapi:2.11.4")
    shadowImplementation("xyz.xenondevs.invui:invui:1.23")
    shadowImplementation("net.wesjd:anvilgui:1.9.0-SNAPSHOT")
    shadowImplementation("com.github.jsixface:yamlconfig:1.2")
    shadowImplementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")
    shadowImplementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    shadowImplementation("com.mysql:mysql-connector-j:8.1.0")
    shadowImplementation("org.mariadb.jdbc:mariadb-java-client:3.3.1")
    shadowImplementation("redis.clients:jedis:4.4.3")
    shadowImplementation("com.google.code.gson:gson:2.10.1")

    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.58.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
}

tasks.processResources {
    from("src/main/resources")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    filesMatching("plugin.yml") {
        expand("version" to version)
    }
}

tasks {
    named<ShadowJar>("shadowJar") {
        mustRunAfter(test)
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

        // EXCLUSIONS
        exclude("colors.bin")
        exclude("waffle/**")
        exclude("com/sun/**")
        exclude("com/google/protobuf/**")
        exclude("com/google/errorprone/**")
        exclude("org/apache/commons/logging/**")
        exclude("org/bstats/**")
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
        }
    }
}

tasks.named("jar").configure {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}