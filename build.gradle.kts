fun properties(key: String) = project.findProperty(key).toString()

plugins {
    java
    id("org.jetbrains.intellij") version "1.4.0"
    id("org.jetbrains.changelog") version "1.3.1"
}

group = properties("kpxPluginGroup")
version = properties("kpxPluginVersion")

repositories {
    mavenCentral()
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
    downloadSources.set(properties("platformDownloadSources").toBoolean())
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

    buildSearchableOptions {
        enabled = false
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "11"
        targetCompatibility = "11"
        options.compilerArgs.add("-Xlint:all")
    }

    patchPluginXml {
        pluginDescription.set(htmlFixer("resources/META-INF/description.html"))
        sinceBuild.set(properties("kpxPluginSinceBuild"))
        untilBuild.set(properties("kpxPluginUntilBuild"))
        changeNotes.set(
                provider {
                    changelog.getLatest().toHTML()
                }
        )
    }

    runPluginVerifier {
        val versions = properties("kpxPluginVerifierIdeVersions")
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
        channels.set(
                listOf(
                        properties("kpxPluginVersion")
                                .split('-')
                                .getOrElse(1) { "default" }
                                .split('.')
                                .first()
                )
        )
    }
}

