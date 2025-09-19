plugins {
    java
    id("org.springframework.boot") version "3.5.4" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

group = "io.github.ddsl.overseermono"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}

dependencies {
    //testImplementation(platform("org.junit:junit-bom:5.10.0"))
    //testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

subprojects {
    if (project.projectDir.path.contains("${rootProject.projectDir}/libs")) {
        layout.buildDirectory = file("${rootProject.projectDir}/dist/libs/${project.name}")
    } else {
        layout.buildDirectory = file("${rootProject.projectDir}/dist/apps/${project.name}")
    }
    repositories {
        mavenCentral()
    }
    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_22.toString()
        targetCompatibility = JavaVersion.VERSION_22.toString()
    }
}

tasks.forEach { task ->
    // Отключаем только задачи корневого проекта
    if (task.project == rootProject) {
        task.enabled = false
    }
}

tasks.register<Delete>("cleanDist") {
    delete("$rootDir/dist")
}