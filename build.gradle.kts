plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.2"
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

group = "xyz.ineanto"
version = "1.2.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    mavenLocal()

    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://repo.codemc.io/repository/maven-snapshots/") }
    maven { url = uri("https://repo.xenondevs.xyz/releases") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.3-R0.1-SNAPSHOT")

    compileOnly("com.github.dmulloy2:ProtocolLib:6845acd89d")
    compileOnly("me.clip:placeholderapi:2.11.5")
    compileOnly("net.kyori:adventure-api:4.17.0")
    compileOnly("xyz.xenondevs.invui:invui:1.41")

    implementation(project(":common"))
    implementation(project(":mappings"))

    implementation("net.wesjd:anvilgui:1.10.3-SNAPSHOT")
    implementation("com.github.jsixface:yamlconfig:1.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.18.1")
    implementation("com.mysql:mysql-connector-j:9.1.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.0")
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
        relocate("me.clip", "xyz.ineanto.nicko.libs.placeholderapi")
        relocate("net.wesjd", "xyz.ineanto.nicko.libs.anvilgui")
        relocate("com.github.jsixface", "xyz.ineanto.nicko.libs.yaml")
        relocate("com.fasterxml.jackson", "xyz.ineanto.nicko.libs.jacksonpr")
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
            exclude(dependency("net.wesjd:.*"))
        }
    }

    runServer {
        /**
         * https://github.com/jpenilla/run-task/issues/56
         *
         * jpenilla:
         * "On 1.20.5+ it makes no sense to waste time obfuscating the jar in development
         * just for it to be immediately deobfuscated.
         * If you have an edge case setup where this makes sense somehow,
         * you can disable plugin jar detection and configure the plugin jars collection yourself."
         *
         * Well, Nicko is an edge case.
         * AnvilGUI and InvUI are still using Spigot Mappings,
         * and I'm stuck using them until they push a major, breaking update.
         */
        //args("-add-plugin=${reobfJar.get().outputJar.path.toAbsolutePath()}")

        downloadPlugins {
            url("https://download.luckperms.net/1554/bukkit/loader/LuckPerms-Bukkit-5.4.139.jar")

            // 1.20 - 1.20.4 testing
            //url("https://github.com/dmulloy2/ProtocolLib/releases/download/5.2.0/ProtocolLib.jar")

            // 1.20.5 - latest testing
            url("https://ci.dmulloy2.net/job/ProtocolLib/lastSuccessfulBuild/artifact/build/libs/ProtocolLib.jar")
        }

        minecraftVersion("1.20.6")
    }
}