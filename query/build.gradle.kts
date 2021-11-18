// 이미 root 프로젝트에서 플러그인 버전을 선언 했으므로 서브모듈에서는 버전을 명시하지 않아야 한다.
plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    // 코틀린 plugin spring 을 사용하기 위해 플러그인을 설정
    kotlin("plugin.spring")
}

val axonVersion = "4.4.9"
val kotlinLoggingVersion = "1.12.5"

dependencies {

//    implementation(group = "", name = "", version = "")

    /** springboot **/
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    /** axon framework **/
    implementation(group = "org.axonframework", name = "axon-spring-boot-starter", version = axonVersion)
    implementation(group = "org.axonframework", name = "axon-configuration", version = axonVersion)

    /** DB / ORM **/
    implementation(group = "mysql", name = "mysql-connector-java")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-jpa")

    /** Common module **/
    implementation(project(":common"))

    /** Logging **/
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")

    /** Retry lib **/
    implementation(group = "org.springframework.retry", name = "spring-retry")

    /** 화면 구현 **/
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    /** Subscription Query 를 위한 리액터 **/
    implementation(group = "io.projectreactor", name = "reactor-core")
}