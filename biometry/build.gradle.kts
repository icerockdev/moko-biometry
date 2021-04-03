/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import java.util.Base64

plugins {
    plugin(Deps.Plugins.androidLibrary)
    plugin(Deps.Plugins.kotlinMultiPlatform)
    plugin(Deps.Plugins.androidExtensions)
    plugin(Deps.Plugins.mobileMultiPlatform)
    plugin(Deps.Plugins.mavenPublish)
    plugin(Deps.Plugins.signing)
}

group = "dev.icerock.moko"
version = Deps.mokoBiometryVersion

dependencies {
    androidLibrary(Deps.Libs.Android.appCompat)
    androidLibrary(Deps.Libs.Android.biometric)

    mppLibrary(Deps.Libs.MultiPlatform.mokoResources)
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    repositories.maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
        name = "OSSRH"

        credentials {
            username = System.getenv("OSSRH_USER")
            password = System.getenv("OSSRH_KEY")
        }
    }

    publications.withType<MavenPublication> {
        // Stub javadoc.jar artifact
        artifact(javadocJar.get())

        // Provide artifacts information requited by Maven Central
        pom {
            name.set("MOKO biometry")
            description.set("Biometry authentication with Touch ID, Face ID from common code with Kotlin Multiplatform Mobile")
            url.set("https://github.com/icerockdev/moko-biometry")
            licenses {
                license {
                    url.set("https://github.com/icerockdev/moko-biometry/blob/master/LICENSE.md")
                }
            }

            developers {
                developer {
                    id.set("Dorofeev")
                    name.set("Andrey Dorofeev")
                    email.set("adorofeev@icerockdev.com")
                }
                developer {
                    id.set("kovalandrew")
                    name.set("Andrey Kovalev")
                    email.set("kovalev@icerockdev.com")
                }
                developer {
                    id.set("Alex009")
                    name.set("Aleksey Mikhailov")
                    email.set("aleksey.mikhailov@icerockdev.com")
                }
            }

            scm {
                connection.set("scm:git:ssh://github.com/icerockdev/moko-biometry.git")
                developerConnection.set("scm:git:ssh://github.com/icerockdev/moko-biometry.git")
                url.set("https://github.com/icerockdev/moko-biometry")
            }
        }
    }
}

signing {
    val signingKeyId: String? = System.getenv("SIGNING_KEY_ID")
    val signingPassword: String? = System.getenv("SIGNING_PASSWORD")
    val signingKey: String? = System.getenv("SIGNING_KEY")?.let { base64Key ->
        String(Base64.getDecoder().decode(base64Key))
    }
    if (signingKeyId != null) {
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        sign(publishing.publications)
    }
}
