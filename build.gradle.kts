import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    id("org.springframework.boot") version "2.5.6" //apply(false)
    id("io.spring.dependency-management") version "1.0.11.RELEASE" //apply(false)
    kotlin("jvm") version "1.5.31" //apply(false)
    kotlin("plugin.spring") version "1.5.31" //apply(false)
}

val repoUsername = "deploy"
val repoPassword = "sbqlwm0601"
val repoUrl = URI("https://nexus.beforesunrize.com/repository/maven-public/")

val commonLibrary = "1.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        credentials {
            username = repoUsername
            password = repoPassword
        }
        url = repoUrl
    }
}

java.sourceCompatibility = JavaVersion.VERSION_11

buildscript {

    val springBootVersion = "2.5.6"

    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
    }
}

allprojects {
    group = "com.cqrs"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "java")

    java.sourceCompatibility = JavaVersion.VERSION_11

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        }
    }

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

project(":command") {
//    dependencies {
//        implementation(project(":common"))
//    }
}

project(":query") {
//    dependencies {
//        implementation(project(":common"))
//    }
}
project(":common")