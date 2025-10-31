plugins {
    id("java")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("io.freefair.lombok") version "9.0.0"
}

group = "io.github.ddsl.overseermono"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(project(":common-lib"))
}

tasks.test {
    useJUnitPlatform()
}