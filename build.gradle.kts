import org.jetbrains.changelog.Changelog
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.intellij.platform") version "2.16.0"
    id("org.jetbrains.changelog") version "2.5.0"
}

group = "de.halirutan.keypromoterx"
version = "2026.1.1"

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
        // Pinned: unpinned resolves to latest. >=1.385 races on parallel verify
        // (ClosedFileSystemException); <1.385 can't read 2026.x layout (false fails).
        // 1.401 = last known-good.
        pluginVerifier("1.401")
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

// Run config for Rider
val runRider by intellijPlatformTesting.runIde.registering {
    localPath = project.layout.dir(project.provider {
        file("/home/patrick/.local/share/JetBrains/Toolbox/apps/rider")
    })
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
