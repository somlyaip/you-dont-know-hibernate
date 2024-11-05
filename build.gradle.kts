group = "dev.somlyaip.blog"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

plugins {
    java
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management")
    id("io.freefair.lombok")
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
