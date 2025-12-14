plugins {
    `kotlin-dsl`
    alias(libs.plugins.versions)
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.gradle.plugin)
}