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

dependencies {
    implementation("io.github.openfeign.querydsl:querydsl-core:${property("querydslVersion")}")
    implementation("io.github.openfeign.querydsl:querydsl-jpa:${property("querydslVersion")}:jakarta")
    annotationProcessor("io.github.openfeign.querydsl:querydsl-apt:${property("querydslVersion")}:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api:${property("jakartaPersistenceApiVersion")}")
    implementation("io.hypersistence:hypersistence-utils-hibernate-63:${property("hypersistenceVersion")}")
}

allprojects {
    repositories {
        mavenCentral()
    }

    // Ensure all modules use Java 21 toolchain to avoid variant mismatch
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        systemProperty("spring.profiles.active", "test")
    }
}
