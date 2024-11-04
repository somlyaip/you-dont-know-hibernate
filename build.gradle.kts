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

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-docker-compose")
    implementation("org.projectlombok:lombok")
    implementation("com.querydsl:querydsl-core:${property("querydslVersion")}")
    implementation("com.querydsl:querydsl-jpa:${property("querydslVersion")}:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:${property("querydslVersion")}:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api:${property("jakartaPersistenceApiVersion")}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
