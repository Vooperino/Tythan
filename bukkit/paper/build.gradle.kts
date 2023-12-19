plugins {
    id("tythan-global-conventions")
    id("vooplv-public-nexus-repo")
}

description = "paper"
dependencies {
    api(project(":common"))
    compileOnly(project(":bukkit:nms:commonNMS"))
    api(project(":bukkit:nms:NMSv1_19_4", "reobf"))
    api(project(":bukkit:nms:NMSv1_20", "reobf"))
    api(project(":bukkit:nms:NMSv1_20_2", "reobf"))
    compileOnly("com.mojang:brigadier:1.0.18")
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-mojangapi:1.20.2-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.1-20231025")

}

tasks.processResources{
    expand("version" to version)
}

tasks.shadowJar {
    dependsOn(":common:shadowJar")
    dependsOn(":bukkit:nms:NMSv1_19_4:reobfJar")
    dependsOn(":bukkit:nms:NMSv1_20:reobfJar")
    dependsOn(":bukkit:nms:NMSv1_20_2:reobfJar")
    archiveBaseName.set("Tythan-Bukkit")
    archiveVersion.set("")
}
