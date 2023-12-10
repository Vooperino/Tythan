# Tythan
Slumbering for 10,000 years just like the greek ones

Next-generation backend infrastructure featuring a common codebase for Bungee (waterfall) and Spigot (paper) level-code development, as well as an easily accessible Mongo-powered backend database exposing features required to share data across multiple independently operating server nodes.

# Compilation
* Use `gradle clean shadowJar`
* Find `Tythan.jar` in `./tythan-dist/build/libs/Tythan.jar`

## Gradle
* Kotlin – `compileOnly("com.github.archemedes.tythan:[type]:[version]")`
* Groovy – `compileOnly 'com.github.archemedes.tythan:[type]:[version]'`

## Maven
```xml
<dependency>
    <groupId>com.github.archemedes.tythan</groupId>
    <artifactId>[type]</artifactId>
    <version>[version]</version>
    <scope>provided</scope>
</dependency>
```