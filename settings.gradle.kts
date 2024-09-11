rootProject.name = "nicko"

setOf(
    "1_20"
).forEach {
    include("mappings:v$it")
}

include("common", "api")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        maven { url = uri("https://jitpack.io") }
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
}
include("v1_20")
