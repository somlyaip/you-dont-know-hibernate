group = "dev.somlyaip.blog.youdontknowhibernate"
version = "0.0.1-SNAPSHOT"

plugins {
    `java-library`
    java
    id("io.spring.dependency-management")
    id("io.freefair.lombok")
}

dependencies {
    api("org.springframework.boot:spring-boot-autoconfigure:${property("springBootVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${property("springBootVersion")}")
    implementation("io.hypersistence:hypersistence-utils-hibernate-63:${property("hypersistenceVersion")}")
    implementation("net.ttddyy:datasource-proxy:${property("datasourceProxyVersion")}")
}
