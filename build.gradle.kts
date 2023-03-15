import org.jetbrains.changelog.Changelog

// fun properties(key: String) = project.findProperty(key).toString()
fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

plugins {
    java
    id("org.jetbrains.intellij") version "1.13.2"
    id("org.jetbrains.changelog") version "2.0.0"
}

group = properties("kpxPluginGroup").get()
version = properties("kpxPluginVersion").get()

repositories {
    mavenCentral()
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

intellij {
    pluginName.set(properties("kpxPluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))
    updateSinceUntilBuild.set(true)
}

changelog {
    version.set(properties("kpxPluginVersion"))
    path.set("${project.projectDir}/CHANGELOG.md")
    header.set("[${properties("kpxPluginVersion")}]")
    // 2019, 2019.2, 2020.1.2
    headerParserRegex.set("""\d+(\.\d+)+""".toRegex())
    itemPrefix.set("-")
    keepUnreleasedSection.set(true)
    unreleasedTerm.set("[Unreleased]")
    groups.set(listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security"))
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

    buildSearchableOptions {
        enabled = false
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-Xlint:all")
    }

    patchPluginXml {
        pluginDescription.set(htmlFixer("resources/META-INF/description.html"))
        sinceBuild.set(properties("kpxPluginSinceBuild"))
        untilBuild.set(properties("kpxPluginUntilBuild"))

        val changelog = project.changelog
        changeNotes.set(properties("kpxPluginVersion").map { pluginVersion ->
            with(changelog) {
                renderItem(
                        (getOrNull(pluginVersion) ?: getUnreleased())
                                .withHeader(false)
                                .withEmptySections(false),
                        Changelog.OutputType.HTML,
                )
            }
        })
    }

    runPluginVerifier {
        val versions = properties("kpxPluginVerifierIdeVersions")
                .get()
                .split(",")
                .map(String::trim)
                .filter(String::isNotEmpty)
        // Kinda useless since the pluginVerifier will cry out
        // anyway, but may not setting a version will be implemented at some point.
        if (versions.isNotEmpty()) {
            ideVersions.set(versions)
        }
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token.set(System.getenv("PUBLISH_TOKEN"))
        // Use beta versions like 2020.3-beta-1
        channels.set(properties("pluginVersion").map {
            listOf(it
                    .split('-')
                    .getOrElse(1) { "default" }
                    .split('.')
                    .first()
            )
        })
    }
}

