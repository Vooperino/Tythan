
plugins {
    id("tythan-global-conventions")
    id("io.papermc.paperweight.userdev") version "1.5.8"
}

val MINECRAFT_VER = "1.19.4-R0.1"
val reobfJar: io.papermc.paperweight.tasks.RemapJar by tasks

dependencies {
    paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:$MINECRAFT_VER-SNAPSHOT")
    compileOnly(project(":common"))
    api(project(":bukkit:nms:commonNMS"))
    compileOnly("io.papermc:paperlib:1.0.8")
    compileOnly("com.mojang:brigadier:1.0.18")
    compileOnly("io.papermc.paper:paper-api:$MINECRAFT_VER-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-mojangapi:$MINECRAFT_VER-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.1-20231025")

}

description = "TythanBukkitNMS$MINECRAFT_VER"

tasks.processResources{
    expand("version" to version)
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}

tasks.reobfJar {
    dependsOn(":common:shadowJar")
    dependsOn(":bukkit:nms:commonNMS:jar")
}

