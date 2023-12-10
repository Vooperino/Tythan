plugins {
    `maven-publish`
    id("idea")
}

publishing {
    publications.create<MavenPublication>("vooplv-public") {
        artifactId = description
        artifact(tasks["shadowJar"])
    }
    repositories {
        maven {
            name = "voopLVPublic"
            url = uri("https://repo.voop.lv/repository/vooplv-public/")
            credentials(PasswordCredentials::class)
        }
    }
}
