plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:2.3.4")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.4")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.4")
    implementation("io.ktor:ktor-server-websockets-jvm:2.3.4")
    implementation("org.jetbrains.exposed:exposed-core:0.45.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.45.0")
    implementation("com.h2database:h2:2.2.224")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("io.ktor:ktor-server-config-yaml:2.3.4")
    implementation("org.jetbrains.exposed:exposed-java-time:0.45.0")
    implementation("org.postgresql:postgresql:42.6.0")
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.23")
    implementation("com.expediagroup:graphql-kotlin-ktor-server:9.0.0-alpha.8")
//    implementation("com.expediagroup:graphql-kotlin-schema-generator:8.8.1")
//    implementation("com.expediagroup:graphql-kotlin-hooks-provider:8.8.1")
}

