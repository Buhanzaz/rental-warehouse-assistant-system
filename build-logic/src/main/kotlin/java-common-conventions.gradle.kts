plugins {
    `java-library`
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
    val junitBom = libs.findLibrary("junit-bom").get().get()
    val junitJupiter = libs.findLibrary("junit-jupiter").get().get()

    compileOnly(lombok)
    annotationProcessor(lombok)

    testImplementation(platform(junitBom))
    testImplementation(junitJupiter)
}

tasks.test {
    useJUnitPlatform()
}
