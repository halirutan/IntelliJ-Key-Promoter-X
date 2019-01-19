import org.jetbrains.intellij.tasks.RunIdeTask

plugins {
  id("org.jetbrains.intellij") version "0.4.1"
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
// tasks.withType(JavaCompile) { options.encoding = "UTF-8" }

intellij {
  version = "LATEST-EAP-SNAPSHOT"
  updateSinceUntilBuild = true
  pluginName = "Key-Promoter-X"
//    intellij.alternativeIdePath = "/usr/local/IntelliJ/android-studio"
//    intellij.alternativeIdePath = "/Applications/Development/Android Studio.app"
//    alternativeIdePath = "/Applications/Development/GoLand.app"
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

version = "5.12"

tasks {
  patchPluginXml {
    changeNotes(htmlFixer("resources/META-INF/change-notes.html"))
    pluginDescription(htmlFixer("resources/META-INF/description.html"))
    sinceBuild("162")
    untilBuild("191.*")
  }

  register<RunIdeTask>("runProfiler") {
    jvmArgs("-agentpath:/home/patrick/build/share/YourKit-JavaProfiler-2018.5/bin/linux-x86-64/libyjpagent.so")
  }

}
