plugins {
    `maven-publish`
    `java-library`
    `signing`
    `jacoco`
    id("io.github.gradle-nexus.publish-plugin")
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.1.0"
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
            artifact(tasks.named("bootJar"))
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
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

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}


tasks {
    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport) // report is always generated after tests run
    }
    jacocoTestReport {
        reports {
            xml.required.set(true)
            xml.outputLocation.set(file("${buildDir}/reports/jacoco/report.xml"))
            html.required.set(true)
            csv.required.set(true)
        }
        dependsOn(test) // tests are required to run before generating the report
    }
}
