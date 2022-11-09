
plugins {
    `maven-publish`
    `java-library`
}

group = "org.octopusden"
version = project.findProperty("buildVersion") ?: "0.0-SNAPSHOT"

java {
    targetCompatibility = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "OSSRH"
            version = System.getenv("BUILD_VERSION")
            if (!project.version.toString().endsWith("-SNAPSHOT")) {
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            } else {
                url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
            }
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}