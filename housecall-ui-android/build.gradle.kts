import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidLibrary)
    `maven-publish`
}

group = "com.housecall"
version = "0.1.1"

kotlin {
    androidTarget {
        publishLibraryVariants("release")
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }
    }
}

android {
    namespace = "com.housecall.designsystem"
    compileSdk = libs.versions.android.compile.sdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.min.sdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

afterEvaluate {
    publishing {
        publications.withType<MavenPublication> {
            groupId = "com.housecall"
            artifactId = "housecall-ui-android"
            version = project.version.toString()
        }
    }
}

publishing {
    repositories {
        // GitHub Packages — public read with PAT (read:packages scope).
        // Credentials resolved in this priority order:
        //   1. Gradle properties: gpr.user / gpr.token (recommended for local devs;
        //      put them in ~/.gradle/gradle.properties so they don't leak into VCS)
        //   2. Env vars: GITHUB_ACTOR / GITHUB_TOKEN (used by GitHub Actions)
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ramzeshcp/housecall-ui-android")
            credentials {
                username = (project.findProperty("gpr.user") as String?)
                    ?: System.getenv("GITHUB_ACTOR")
                password = (project.findProperty("gpr.token") as String?)
                    ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
