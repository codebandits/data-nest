import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by project.extra
val junitJupiterVersion: String by project.extra
val assertjVersion: String by project.extra
val exposedVersion: String by project.extra
val h2Version: String by project.extra
val resultsVersion: String by project.extra

buildscript {
    val junitPlatformVersion: String by project.extra
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:$junitPlatformVersion")
    }
}

plugins {
    val kotlinVersion = "1.1.51"
    kotlin("jvm") version kotlinVersion
}

apply {
    plugin("org.junit.platform.gradle.plugin")
    plugin("maven")
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
    maven("https://dl.bintray.com/kotlin/exposed")
    maven("https://jitpack.io")
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion")

    compile("org.jetbrains.exposed:exposed:$exposedVersion")
    testRuntime("com.h2database:h2:$h2Version")

    compile("com.github.codebandits.results:results:$resultsVersion")
    testCompile("com.github.codebandits.results:results-test:$resultsVersion")

    testCompile("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testCompile("org.assertj:assertj-core:$assertjVersion")
}
