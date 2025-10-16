group = "dev.somlyaip.blog.youdontknowhibernate"
version = "0.0.1-SNAPSHOT"

plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("io.freefair.lombok")
}

dependencies {
    implementation(project(":common-starter"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-docker-compose")
    implementation("org.projectlombok:lombok")
    implementation("io.github.openfeign.querydsl:querydsl-core:${property("querydslVersion")}")
    implementation("io.github.openfeign.querydsl:querydsl-jpa:${property("querydslVersion")}:jakarta")
    annotationProcessor("io.github.openfeign.querydsl:querydsl-apt:${property("querydslVersion")}:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("io.hypersistence:hypersistence-utils-hibernate-63:${property("hypersistenceVersion")}")
}