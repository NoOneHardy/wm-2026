plugins {
  java
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

dependencies {
  runtimeOnly(libs.mysql.connector)

  implementation(libs.spring.boot.starter.web)
  implementation(libs.spring.boot.starter.actuator)
  implementation(libs.spring.boot.starter.validation)
  implementation(libs.spring.boot.starter.data.jpa)
  implementation(libs.spring.boot.starter.security)

  implementation(libs.io.jsonwebtoken.jjwt.api)
  implementation(libs.io.jsonwebtoken.jjwt.impl)
  implementation(libs.io.jsonwebtoken.jjwt.jackson)

  implementation(libs.mapstruct)
  annotationProcessor(libs.mapstruct.processor)

  compileOnly(libs.lombok)
  annotationProcessor(libs.lombok)
  annotationProcessor(libs.lombok.mapstruct.binding)

  testCompileOnly(libs.lombok)
  testAnnotationProcessor(libs.lombok)
  testAnnotationProcessor(libs.lombok.mapstruct.binding)

  implementation(libs.slf4j.api)

  testImplementation(libs.spring.boot.starter.test)
  testImplementation(libs.spring.security.test)
  testImplementation(libs.spring.boot.webmvc.test)
  testImplementation(libs.spring.boot.data.jpa.test)
  testRuntimeOnly(libs.h2)

  implementation(libs.jakarta.mail)
}

tasks.withType<Test> {
  useJUnitPlatform()
}
