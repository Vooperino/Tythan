plugins {
    `java-library`
    id("idea")
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}


repositories {
    maven { url = uri("https://repo.voop.lv/repository/vooplvPublic/") }
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { url = uri("https://libraries.minecraft.net") }
    maven { url = uri("https://repo.maven.apache.org/maven2/") }
    maven { url = uri("https://maven.enginehub.org/repo/") }
    maven { url = uri("https://repo.md-5.net/content/repositories/releases/") }
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-oss-snapshots"
    }
    maven(url = "https://repo.velocitypowered.com/snapshots/") {
        name = "velocity"
    }
    maven { url = uri("https://mvn.exceptionflug.de/repository/exceptionflug-public/") }
    maven { url = uri("https://repo.dmulloy2.net/repository/public/") }
    maven { url = uri("https://repo.viaversion.com/everything/") }
    maven { url = uri("https://repo.aikar.co/content/groups/aikar/") }
    maven { url = uri("https://jitpack.io/") }
    maven { url = uri("https://repo.ranull.com/maven/external/") }
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") }
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}