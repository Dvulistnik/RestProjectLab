plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.20"
    application
}

group = "org.dvulist.restproject"
version = "1.0.0"
application {
    mainClass.set("org.dvulist.restproject.ServerKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

tasks {
    shadowJar {
        archiveFileName.set("rest-server.jar")

        manifest {
            attributes(mapOf("Main-Class" to application.mainClass.get()))
        }

        mergeServiceFiles()
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation("org.jetbrains.exposed:exposed-core:0.48.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.48.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.48.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.48.0")
    implementation("org.xerial:sqlite-jdbc:3.46.0.0") // Драйвер JDBC для SQLite
}