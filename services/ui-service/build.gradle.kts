plugins {
    id("spring-boot-common-conventions")
    alias(libs.plugins.vaadin)
}

repositories {
    maven(url = "https://maven.vaadin.com/vaadin-prereleases")
    maven(url = "https://repo.spring.io/milestone")
    maven(url = "https://maven.vaadin.com/vaadin-addons")
}

configurations {
    maybeCreate("developmentOnly")
//
//    val developmentOnly by getting
//
//    val runtimeClasspath by getting {
//        extendsFrom(developmentOnly)
//    }
}

dependencies {
    implementation(libs.vaadin.dev)
    implementation(libs.vaadin.spring.boot.starter)
    developmentOnly(libs.spring.boot.devtools)
}

dependencyManagement {
    imports {
        mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
    }
}

tasks.named("processResources") {
    dependsOn("vaadinPrepareFrontend")
}
