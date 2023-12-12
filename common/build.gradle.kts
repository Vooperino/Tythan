plugins {
    id("tythan-global-conventions")
}

description = "common"
dependencies {
    compileOnly("com.mojang:brigadier:1.0.18")
}

tasks.shadowJar {
    archiveBaseName.set("Tythan-Commons")
    archiveVersion.set("")
    relocate("com.electronwill.nightconfig.core","com.github.archemedes.tythan.shade.electronwill.nightconfig.core")
    relocate("com.electronwill.nightconfig.yaml","com.github.archemedes.tythan.shade.electronwill.nightconfig.yaml")
    relocate("com.electronwill.nightconfig.toml","com.github.archemedes.tythan.shade.electronwill.nightconfig.toml")
    relocate("com.electronwill.nightconfig.json","com.github.archemedes.tythan.shade.electronwill.nightconfig.json")
}
