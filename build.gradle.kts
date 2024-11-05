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
    implementation("com.querydsl:querydsl-core:${property("querydslVersion")}")
    implementation("com.querydsl:querydsl-jpa:${property("querydslVersion")}:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:${property("querydslVersion")}:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api:${property("jakartaPersistenceApiVersion")}")
    implementation("io.hypersistence:hypersistence-utils-hibernate-63:${property("hypersistenceVersion")}")
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
