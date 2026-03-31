import org.jetbrains.changelog.Changelog
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.intellij.platform") version "2.13.2-SNAPSHOT"
    id("org.jetbrains.changelog") version "2.5.0"
}

group = "de.halirutan.keypromoterx"
version = "2026.1"

val repoURL = "https://github.com/halirutan/IntelliJ-Key-Promoter-X"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdea("2026.1")
        testFramework(TestFrameworkType.Platform)
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
    instrumentCode = true

    pluginConfiguration {
        name = "Key Promoter X"
        description = htmlFixer("resources/META-INF/description.html")
        changeNotes = project.version.toString().let { pluginVersion ->
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
            url = repoURL
        }

        ideaVersion {
            sinceBuild = "241"
        }
    }

    publishing {
        token = System.getenv("PUBLISH_TOKEN")

        // Use beta versions like 2020.3-beta-1
        channels = listOf(
            version.toString()
                .split('-')
                .getOrElse(1) { "default" }
                .split('.')
                .first()
        )
    }
}

changelog {
    groups.empty()
    repositoryUrl.set(repoURL)
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
