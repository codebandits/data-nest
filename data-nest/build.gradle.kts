import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
    val junitPlatformVersion = "1.0.1"
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

val junitJupiterVersion = "5.0.1"
val assertjVersion = "3.8.0"
val exposedVersion = "0.8.6"
val h2Version = "1.4.196"
val resultsVersion = "9fca3475d5"

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jre8")

    compile("org.jetbrains.exposed:exposed:$exposedVersion")
    testRuntime("com.h2database:h2:$h2Version")

    compile("com.github.codebandits.results:results:$resultsVersion")
    testCompile("com.github.codebandits.results:results-test:$resultsVersion")

    testCompile("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testCompile("org.assertj:assertj-core:$assertjVersion")
}
