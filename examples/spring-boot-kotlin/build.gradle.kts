import org.gradle.kotlin.dsl.extra
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val junitJupiterVersion: String by project.extra
val assertjVersion: String by project.extra
val exposedVersion: String by project.extra
val h2Version: String by project.extra
val resultsVersion: String by project.extra

buildscript {
    val springBootVersion: String by project.extra
    val junitPlatformVersion: String by project.extra
    repositories {
        mavenCentral()
        maven("https://repo.spring.io/milestone")
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
        classpath("org.junit.platform:junit-platform-gradle-plugin:$junitPlatformVersion")
    }
}

plugins {
    val kotlinVersion = "1.1.51"
    kotlin("jvm") version kotlinVersion
}

apply {
    plugin("org.springframework.boot")
    plugin("io.spring.dependency-management")
    plugin("org.junit.platform.gradle.plugin")
}

group = "io.github.codebandits"
version = "1.0-SNAPSHOT"

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

repositories {
    mavenCentral()
    maven("https://repo.spring.io/milestone")
    maven("https://dl.bintray.com/kotlin/exposed")
    maven("https://jitpack.io")
}

dependencies {
    compile(kotlin("stdlib-jre8"))

    compile("org.springframework.boot:spring-boot-starter-web")

    compile(project(":data-nest"))
    compile("org.jetbrains.exposed:spring-transaction:$exposedVersion")
    runtime("com.h2database:h2:$h2Version")

    compile("com.github.codebandits.results:results:$resultsVersion")
    testCompile("com.github.codebandits.results:results-test:$resultsVersion")

    testCompile("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
    }
    testCompile("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testCompile("org.assertj:assertj-core:$assertjVersion")
}
