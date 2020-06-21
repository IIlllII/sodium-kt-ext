plugins {
    id("org.jetbrains.kotlin.js") version "1.3.72"
    id("maven-publish")
}

group = "com.bitbreeds.crypto"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
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


publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/IIlllII/sodium-kt-ext")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {

            from(components["kotlin"])
            pom {
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
                    url.set("https://github.com/IIlllII/sodium-kt-ext.git")
                }
            }
        }
    }
}