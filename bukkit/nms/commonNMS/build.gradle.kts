plugins {
    id("tythan-global-conventions")
}

dependencies {
    compileOnly(project(":common"))
    compileOnly("com.mojang:brigadier:1.0.18")
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-mojangapi:1.19.4-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.1-20231025")
}

description = "TythanBukkitNMSCommons"

tasks.shadowJar {
    dependsOn(":common:shadowJar")
    archiveBaseName.set("TythanBukkitNMSCommons")
    archiveVersion.set("")
}