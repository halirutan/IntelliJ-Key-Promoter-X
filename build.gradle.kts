buildscript {
  repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://dl.bintray.com/jetbrains/intellij-plugin-service")

  }
  dependencies {
    classpath("org.jetbrains.intellij.plugins:gradle-intellij-plugin:0.5.0-SNAPSHOT")
  }
}

plugins {
  id("java")
}

apply(plugin = "org.jetbrains.intellij")

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}


repositories {
  mavenCentral()
}

sourceSets {
  main {
    java.srcDir("src")
    resources.srcDir("resources")
  }
}
// tasks.withType(JavaCompile) { options.encoding = "UTF-8" }

configure<org.jetbrains.intellij.IntelliJPluginExtension> {
  version = "LATEST-EAP-SNAPSHOT"
  updateSinceUntilBuild = true
  pluginName = "Key-Promoter-X"
//  alternativeIdePath = "/home/patrick/.local/share/JetBrains/Toolbox/apps/AndroidStudio/ch-0/183.5452501"
//  alternativeIdePath = "/home/patrick/.local/share/JetBrains/Toolbox/apps/PyCharm-P/ch-0/191.6605.12"
//  alternativeIdePath = "/usr/local/IntelliJ/android-studio"
//  alternativeIdePath = "/home/patrick/.local/share/JetBrains/Toolbox/apps/WebStorm/ch-0/191.6707.60"
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

version = "2019.2.3"

tasks {
  named<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    changeNotes(htmlFixer("resources/META-INF/change-notes.html"))
    pluginDescription(htmlFixer("resources/META-INF/description.html"))
    sinceBuild("191")
  }

  named<org.jetbrains.intellij.tasks.PublishTask>("publishPlugin") {
    if (project.hasProperty("pluginsToken")) {
      token(project.property("pluginsToken"))
    }
  }
}

