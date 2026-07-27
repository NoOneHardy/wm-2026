import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
  alias(libs.plugins.spring.boot) apply false
  alias(libs.plugins.spring.dependency.management) apply false
}

subprojects {
  plugins.withType<JavaPlugin> {
    extensions.configure<JavaPluginExtension> {
      toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
      }
    }
  }

  plugins.withType<SpringBootPlugin> {
    val dotenv = layout.projectDirectory.file(".env")
    val dotenvVars = providers.fileContents(dotenv).asText.map { text ->
      text.lineSequence()
        .map(String::trim)
        .filter { it.isNotEmpty() && !it.startsWith('#') && it.contains("=")}
        .associate { it.substringBefore("=").trim() to it.substringAfter("=").trim() }
    }.orElse(emptyMap())

    tasks.withType<BootRun> {
      environment(dotenvVars.get())
    }
  }
}