plugins {
    application
    kotlin("jvm") version "1.5.30"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
    mavenLocal()
    google()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
    maven { url = uri("https://plugins.gradle.org/m2/") }
    maven { url = uri("https://dl.bintray.com/ekito/koin") }
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("io.ktor:ktor-server-core:${Versions.ktor}")
    implementation("io.ktor:ktor-server-host-common:${Versions.ktor}")
    implementation("io.ktor:ktor-gson:${Versions.ktor}")
    implementation("io.ktor:ktor-server-netty:${Versions.ktor}")
    implementation("io.ktor:ktor-auth:${Versions.ktor}")
    implementation("io.ktor:ktor-auth-jwt:${Versions.ktor}")

    implementation("io.insert-koin:koin-ktor:${Versions.koin}")
    implementation("io.insert-koin:koin-logger-slf4j:${Versions.koin}")

    implementation("org.postgresql:postgresql:${Versions.postgreSql}")
    implementation("com.zaxxer:HikariCP:${Versions.hikari}")
    implementation("org.flywaydb:flyway-core:${Versions.flyway}")

    implementation("org.jetbrains.exposed:exposed-core:${Versions.exposed}")
    implementation("org.jetbrains.exposed:exposed-dao:${Versions.exposed}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}")
    implementation("org.jetbrains.exposed:exposed-java-time:${Versions.exposed}")

    implementation("org.valiktor:valiktor-core:${Versions.valiktor}")

    implementation("com.github.ben-manes.caffeine:caffeine:${Versions.caffeine}")

    implementation("ch.qos.logback:logback-classic:${Versions.logback}")

    implementation("com.google.firebase:firebase-admin:${Versions.firebase}")

    testImplementation("io.ktor:ktor-server-tests:${Versions.ktor}")
    testImplementation("io.mockk:mockk:${Versions.mockk}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${Versions.junit5}")
    testImplementation("org.junit.jupiter:junit-jupiter:${Versions.junit5}")

    testImplementation("org.testcontainers:junit-jupiter:${Versions.testContainerVersion}")
    testImplementation("org.testcontainers:testcontainers:${Versions.testContainerVersion}")
    testImplementation("org.testcontainers:postgresql:${Versions.testContainerVersion}")

    testImplementation("io.kotest:kotest-assertions-ktor:${Versions.kotest}")
    testImplementation("io.kotest:kotest-assertions-core:${Versions.kotest}")

    testImplementation("io.insert-koin:koin-test-junit5:${Versions.koin}")
    testImplementation("io.insert-koin:koin-test:${Versions.koin}")
}