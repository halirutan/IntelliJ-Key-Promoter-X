import org.jetbrains.changelog.Changelog

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

plugins {
    id("java") // Java support
    alias(libs.plugins.gradleIntelliJPlugin) // Gradle IntelliJ Plugin
    alias(libs.plugins.changelog) // Gradle Changelog Plugin
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    implementation(libs.annotations)
    intellijPlatform {
        val type = properties("platformType").get()
        val version = properties("platformVersion").get()
        create(type, version)
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

sourceSets {
    main {
        java.srcDir("src")
        resources.srcDir("resources")
    }
}

intellijPlatform {
    buildSearchableOptions = false
    instrumentCode = false
    projectName = project.name

    pluginConfiguration {
        id = properties("pluginGroup")
        name = properties("pluginName")
        version = properties("pluginVersion")
        description = htmlFixer("resources/META-INF/description.html")
        changeNotes = properties("pluginVersion").map { pluginVersion ->
            with(changelog) {
                renderItem(
                    (getOrNull(pluginVersion) ?: getUnreleased())
                        .withHeader(false)
                        .withEmptySections(false),
                    Changelog.OutputType.HTML,
                )
            }
        }

        vendor {
            name = "halirutan"
            url = "https://github.com/halirutan/IntelliJ-Key-Promoter-X"
        }

        ideaVersion {
            sinceBuild = properties("pluginSinceBuild")
            untilBuild = properties("pluginUntilBuild")
        }
    }

    verifyPlugin {
        val versions = properties("pluginVerifierIdeVersions")
            .get()
            .split(",")
            .map(String::trim)
            .filter(String::isNotEmpty)
        ides {
            versions.map { ide(it) }
        }
    }

    publishing {
        token = System.getenv("PUBLISH_TOKEN")

        // Use beta versions like 2020.3-beta-1
        channels = properties("pluginVersion").map {
            listOf(it
                .split('-')
                .getOrElse(1) { "default" }
                .split('.')
                .first()
            )
        }
    }

}

changelog {
    groups.empty()
    repositoryUrl.set(properties("pluginRepositoryUrl"))
}

/**
 * Simple function to load HTML files and remove the surrounding `<html>` tags. This is useful for maintaining changes-notes
 * and the description of plugins in separate HTML files which makes them much more readable.
 */
fun htmlFixer(filename: String): String {
    if (!File(filename).exists()) {
        logger.error("File $filename not found.")
    } else {
        return File(filename).readText().replace("<html>", "").replace("</html>", "")
    }
    return ""
}

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion").get()
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-Xlint:all")
    }

    publishPlugin {
        dependsOn("patchChangelog")
    }
}

