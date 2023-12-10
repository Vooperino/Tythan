plugins {
    id("tythan-global-conventions")
    id("vooplv-public-nexus-repo")
}

description = "common"
dependencies {
    compileOnly("com.mojang:brigadier:1.0.18")
}

tasks.shadowJar {
    archiveBaseName.set(project.description)
}
