plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

java {
    toolchain {
        val javaVersion = libs.findVersion("java").get().requiredVersion.toInt()
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    val lombok = libs.findLibrary("lombok").get().get()

    val springBootStarter = libs.findLibrary("spring-boot-starter").get()
    val springBootStarterTest = libs.findLibrary("spring-boot-starter-test").get()

    compileOnly(lombok)
    annotationProcessor(lombok)

    implementation(springBootStarter)
    testImplementation(springBootStarterTest)
}

tasks.test {
    useJUnitPlatform()
}