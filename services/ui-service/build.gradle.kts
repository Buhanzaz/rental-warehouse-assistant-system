plugins {
    id("spring-boot-common-conventions")
    libs.plugins.vaadin
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

    implementation("com.vaadin:vaadin-spring-boot-starter")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

dependencyManagement {
    imports {
        mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
    }
}
