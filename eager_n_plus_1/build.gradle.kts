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
    implementation("com.querydsl:querydsl-core:${property("querydslVersion")}")
    implementation("com.querydsl:querydsl-jpa:${property("querydslVersion")}:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:${property("querydslVersion")}:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api:${property("jakartaPersistenceApiVersion")}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}