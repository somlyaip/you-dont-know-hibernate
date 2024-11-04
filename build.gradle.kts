group = "dev.somlyaip.blog"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// TODO: use extra[] map to get spring boot version from properties
plugins {
    java
    id("org.springframework.boot") version "3.3.5" apply false
    id("io.spring.dependency-management") version "1.1.6"
    id("io.freefair.lombok") version "8.10.2"
}

allprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        systemProperty("spring.profiles.active", "test")
    }
}
