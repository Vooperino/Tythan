rootProject.name = "Tythan"

include(":common")
include(":bukkit:paper")
include(":bukkit:nms:commonNMS")
include(":bukkit:nms:NMSv1_19_4")
include(":bukkit:nms:NMSv1_20")
include(":bukkit:nms:NMSv1_20_2")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
