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
}

val junitJupiterVersion = "5.0.1"

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jre8")

    testCompile("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
}
