plugins {
    id("com.github.johnrengelman.shadow") version "8.1.0"
    id("io.papermc.paperweight.userdev") version "1.5.8" apply false
}

repositories {
    mavenCentral()
}

allprojects {
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "java")
    group = "com.github.archemedes.tythan"
    version = "1.0.0-SNAPSHOT"
}