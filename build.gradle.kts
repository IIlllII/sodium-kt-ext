plugins {
    id("org.jetbrains.kotlin.js") version "1.3.72"
    id("maven-publish")
    id("org.jetbrains.dokka") version "0.10.1"
    signing
}

group = "com.bitbreeds.crypto"
version = "0.1.2-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-js"))
    testImplementation(kotlin("test-js"))
}

kotlin.target.browser {

    testTask {
        useKarma {
            useChromeHeadless()
        }
    }

}

tasks.dokka {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
}

val sourcesJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles source JAR"
    archiveClassifier.set("sources")
    from(kotlin.sourceSets["main"].kotlin)
}

publishing {

    repositories {
        maven {
            credentials {
                username = project.property("ossrhMavenUser") as String
                password = project.property("ossrhMavenPassword") as String
            }
            name = "ossrh"
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
        }
    }
    publications {
        create<MavenPublication>("maven") {

            artifact(tasks.JsJar.get())
            artifact(sourcesJar)
            artifact(dokkaJar)

            pom {
                packaging = "jar"
                name.set(rootProject.name)
                description.set("Kotlin definitions for sodium")
                url.set("https://github.com/IIlllII/sodium-kt-ext")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("Jonas")
                        name.set("Jonas Waage")
                        email.set("jonas.waage@bitbreeds.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/IIlllII/sodium-kt-ext.git")
                    developerConnection.set("scm:git:ssh://github.com/IIlllII/sodium-kt-ext.git")
                    url.set("https://github.com/IIlllII/sodium-kt-ext.git")
                }
            }
        }
    }
}

if (project.hasProperty("release")) {

    signing {
        useGpgCmd()
        sign(publishing.publications["maven"])
    }

}
