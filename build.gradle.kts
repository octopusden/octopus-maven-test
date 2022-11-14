
plugins {
    `maven-publish`
    `java-library`
    `signing`
    id("io.github.gradle-nexus.publish-plugin")
}

group = "org.octopusden"
version = System.getenv("BUILD_VERSION") ?: "0.0-SNAPSHOT"

java {
    targetCompatibility = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11
    withJavadocJar()
    withSourcesJar()
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(System.getenv("MAVEN_USERNAME"))
            password.set(System.getenv("MAVEN_PASSWORD"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set(project.name)
                description.set("Octopus module for testing Maven release workflow")
                url.set("https://github.com/kzaporozhtsev/octopus-maven-test.git")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                scm {
                    url.set("https://github.com/kzaporozhtsev/octopus-maven-test.git")
                    connection.set("scm:git://github.com/kzaporozhtsev/octopus-maven-test.git")
                }
                developers {
                    developer {
                        id.set("octopus")
                        name.set("octopus")
                    }
                }
            }
        }
    }
}

if (!project.version.toString().endsWith("SNAPSHOT", true)) {
    signing {
        sign(publishing.publications["mavenJava"])
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(
            signingKey,
            signingPassword
        )
    }
}