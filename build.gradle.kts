plugins {
  idea apply true
  id("org.jetbrains.intellij") version "0.4.21"
  id("java")
}

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

configure<org.jetbrains.intellij.IntelliJPluginExtension> {
  version = "LATEST-EAP-SNAPSHOT"
  updateSinceUntilBuild = true
  pluginName = "Key-Promoter-X"
//  alternativeIdePath = "/home/patrick/build/share/JetBrains/Toolbox/apps/Rider/ch-0/201.7846.1"
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

version = "2020.2.1"

tasks {
  withType(JavaCompile::class.java) {
    options.encoding = "UTF-8"
  }

  withType(org.jetbrains.intellij.tasks.PatchPluginXmlTask::class.java) {
    changeNotes(htmlFixer("resources/META-INF/change-notes.html"))
    pluginDescription(htmlFixer("resources/META-INF/description.html"))
    sinceBuild("201")
  }

  withType(org.jetbrains.intellij.tasks.PublishTask::class.java) {
    if (project.hasProperty("pluginsToken")) {
      token(project.property("pluginsToken"))
    }
    channels("default")
  }
}

