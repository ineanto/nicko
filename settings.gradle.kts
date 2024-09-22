rootProject.name = "nicko"

include(":mappings")
setOf(
    "1_20", "1_20_2", "1_20_4", "1_20_6",
    "1_21"
).forEach {
    include(":mappings:v$it")
}
