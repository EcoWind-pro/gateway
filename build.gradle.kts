plugins {
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.spring") version "2.0.10"
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.6"
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"

}

group = "ecowind.ru"
version = "1.0"

java.toolchain.languageVersion = JavaLanguageVersion.of(17)
kotlin.compilerOptions.freeCompilerArgs.addAll("-Xjsr305=strict")

val coroutinesVersion = "1.9.0"
val serializationVersion = "1.6.2"
val authapiVersion = "1.0.0"
val clientapiVersion = "1.0.0"
val exceptionshandlerVersion = "1.0.0"
val utilsVersion = "1.0.0"
val detektVersion = "1.23.7"
val nettyVersion = "4.1.85.Final"
val springCloudVersion by extra("2023.0.1")

repositories {
    mavenCentral()
    mavenLocal()
}

detekt {
    config = files("src/main/resources/detekt.yaml")
    autoCorrect = true
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("ecowind.ru:authapi:$authapiVersion")
    implementation("ecowind.ru:clientapi:$clientapiVersion")
    implementation("ecowind.ru:exceptionhandler:$exceptionshandlerVersion")
    implementation("ecowind.ru:utils:$utilsVersion")

    implementation("io.zipkin.reporter2:zipkin-reporter-brave")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-rules-libraries:$detektVersion")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-rules-ruleauthors:$detektVersion")

    implementation("io.netty:netty-all:$nettyVersion")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
