import com.github.gradle.node.pnpm.task.PnpmTask

plugins {
  alias(libs.plugins.node.gradle)
}

node {
  download = true
  version.set(libs.versions.node.version.get())
  pnpmVersion.set(libs.versions.pnpm.version.get())
}

val pnpmInstall = tasks.named<PnpmTask>("pnpmInstall")

val lint = tasks.register<PnpmTask>("lint") {
  description = "Lints project"
  group = "lifecycle"
  dependsOn(pnpmInstall)
  args.addAll(listOf("run", "lint"))
}

val test = tasks.register<PnpmTask>("test") {
  description = "Run unit tests"
  group = "lifecycle"
  dependsOn(pnpmInstall)
  args.addAll(listOf("run", "test-ci"))
}

val ngBuild = tasks.register<PnpmTask>("ngBuild") {
  description = "Build frontend"
  group = "lifecycle"
  dependsOn(pnpmInstall)
  args.addAll(listOf("run", "build"))
}
