rootProject.name = "rental-warehouse-assistant-system"

pluginManagement {
    repositories {
        maven(url = "https://maven.vaadin.com/vaadin-prereleases")
        gradlePluginPortal()
    }
    plugins {
        id("com.vaadin") version providers.gradleProperty("vaadinVersion").get()
    }
}



includeBuild("build-logic")

include(
    ":api",
    ":shared",
    ":services:doc-service",
    ":services:estimate-draft-service",
    ":services:estimate-service",
    ":services:image-ya-disk-service",
    ":services:llm-service",
    ":services:s3-service",
    ":services:ui-service"
)
