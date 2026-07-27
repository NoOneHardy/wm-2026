rootProject.name = "beticon"

pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}

include("frontend")
project(":frontend").projectDir.mkdirs()

include("proxy")
project(":proxy").projectDir.mkdirs()

include("service")
project(":service").projectDir.mkdirs()
