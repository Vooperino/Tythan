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

val jar: Jar by tasks
jar.enabled = true

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

    compileOnly("net.kyori:adventure-api:4.15.0-SNAPSHOT")
    compileOnly("net.kyori:adventure-text-serializer-plain:4.15.0-SNAPSHOT")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.15.0-SNAPSHOT")
    compileOnly("net.kyori:adventure-text-serializer-gson:4.15.0-SNAPSHOT")
    compileOnly("net.kyori:adventure-text-minimessage:4.15.0-SNAPSHOT")

    api("commons-lang:commons-lang:2.6")
    api("commons-io:commons-io:2.11.0")
    api("commons-codec:commons-codec:1.15")
    api("joda-time","joda-time","2.10.10")

    api("com.google.inject:guice:5.1.0")

    api("com.electronwill.night-config:core:3.6.7") {
        exclude("org.yaml","snakeyaml")
    }
    api("com.electronwill.night-config:yaml:3.6.7") {
        exclude("org.yaml","snakeyaml")
    }
    api("com.electronwill.night-config:toml:3.6.7") {
        exclude("org.yaml","snakeyaml")
    }
    api("com.electronwill.night-config:json:3.6.7") {
        exclude("org.yaml","snakeyaml")
    }

}