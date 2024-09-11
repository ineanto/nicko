plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("io.github.goooler.shadow") version "8.1.7"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("com.github.dmulloy2:ProtocolLib:master-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.5")
    compileOnly("net.kyori:adventure-api:4.17.0")

    rootProject.subprojects.forEach {
        if (!it.name.startsWith("v")) return@forEach

        compileOnly(project(":mappings:${it.name}"))
    }

    implementation("xyz.xenondevs.invui:invui:1.35")
    implementation("net.wesjd:anvilgui:1.10.1-SNAPSHOT")
    implementation("com.github.jsixface:yamlconfig:1.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    implementation("com.mysql:mysql-connector-j:8.2.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.1")
    implementation("redis.clients:jedis:5.1.2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.bstats:bstats-bukkit:3.0.2")
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

    runServer {
        dependsOn(shadowJar)

        downloadPlugins {
            url("https://download.luckperms.net/1554/bukkit/loader/LuckPerms-Bukkit-5.4.139.jar")

            // 1.20 - 1.20.4 testing
            //url("https://github.com/dmulloy2/ProtocolLib/releases/download/5.2.0/ProtocolLib.jar")

            // 1.20.5 - latest testing
            url("https://ci.dmulloy2.net/job/ProtocolLib/lastSuccessfulBuild/artifact/build/libs/ProtocolLib.jar")
        }

        minecraftVersion("1.21.1")
    }
}