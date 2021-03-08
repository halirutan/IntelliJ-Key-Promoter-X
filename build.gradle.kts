import org.jetbrains.changelog.closure
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.intellij.tasks.BuildSearchableOptionsTask
import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.intellij.tasks.PublishTask

plugins {
    java
    id("org.jetbrains.intellij") version "0.7.2"
    id("org.jetbrains.changelog") version "1.1.2"
}

val kpxPluginGroup: String by project
val kpxPluginName: String by project
val kpxPluginVersion: String by project
val kpxPluginSinceBuild: String by project
val kpxPluginUntilBuild: String by project
val kpxPluginVerifierIdeVersions: String by project

val platformType: String by project
val platformVersion: String by project
val platformPlugins: String by project
val platformDownloadSources: String by project

//java {
//    sourceCompatibility = JavaVersion.VERSION_11
//    targetCompatibility = JavaVersion.VERSION_11
//}

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java.srcDir("src")
        resources.srcDir("resources")
    }
}

configure<IntelliJPluginExtension> {
    version = platformVersion
    type = platformType
    downloadSources = platformDownloadSources.toBoolean()
    updateSinceUntilBuild = true
    pluginName = kpxPluginName
}

changelog {
    version = "${project.version}"
    path = "${project.projectDir}/CHANGELOG.md"
    header = closure { "[${project.version}]" }
    // 2019, 2019.2, 2020.1.2
    headerParserRegex = """\d+(\.\d+)+""".toRegex()
    itemPrefix = "-"
    keepUnreleasedSection = true
    unreleasedTerm = "[Unreleased]"
    groups = listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security")
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

group = kpxPluginGroup
version = kpxPluginVersion

tasks {

    withType<BuildSearchableOptionsTask> {
        enabled = false
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    withType<PatchPluginXmlTask> {
        pluginDescription(htmlFixer("resources/META-INF/description.html"))
        sinceBuild(kpxPluginSinceBuild)
        untilBuild(kpxPluginUntilBuild)
        changeNotes(
                closure {
                    changelog.getLatest().toHTML()
                }
        )
    }

    runPluginVerifier {
        ideVersions(kpxPluginVerifierIdeVersions)
    }

    withType<PublishTask> {
        dependsOn("patchChangelog")
        token(System.getenv("PUBLISH_TOKEN"))
        // Use beta versions like 2020.3-beta-1
        channels(kpxPluginVersion.split('-').getOrElse(1) { "default" }.split('.').first())
    }
}

