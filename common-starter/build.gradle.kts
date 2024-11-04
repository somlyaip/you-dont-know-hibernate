group = "dev.somlyaip.blog.youdontknowhibernate"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

plugins {
    `java-library`
    java
    id("io.spring.dependency-management")
    id("io.freefair.lombok")
}

dependencies {
    api("org.springframework.boot:spring-boot-autoconfigure:${property("springBootVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${property("springBootVersion")}")
}
