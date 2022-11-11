
plugins {
    `maven-publish`
    `java-library`
    id("io.github.gradle-nexus.publish-plugin")
}

group = "org.octopusden"
version = System.getenv("BUILD_VERSION") ?: "0.0-SNAPSHOT"

java {
    targetCompatibility = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11
}

nexusPublishing {
    repositories {
        sonatype {
            username.set(System.getenv("MAVEN_USERNAME"))
            password.set(System.getenv("MAVEN_PASSWORD"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}