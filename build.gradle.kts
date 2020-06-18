plugins {
    id("org.jetbrains.kotlin.js") version "1.3.72"
    id("maven-publish")
}



group = "com.bitbreeds.p2p"
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
            useFirefox()
        }
    }

}


publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
        }
    }
}