import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.google.services)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    jacoco
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
        iosTarget.compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }
    }

    // Add global compiler options for all targets
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(compose.uiTooling)
            implementation(libs.androidx.activity.compose)
            // Firebase dependencies - using libs references
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.auth)
            implementation(libs.firebase.firestore)
            implementation(libs.firebase.analytics)
            // Modern Google Sign-In using Credential Manager API
            implementation(libs.androidx.credentials)
            implementation(libs.androidx.credentials.play.services)
            implementation(libs.googleid)
            // Koin for Android
            implementation(libs.koin.android)
            implementation(libs.koin.compose)

            // Android-specific network implementation for Coil
            implementation(libs.coil.network.okhttp)

            // Navigation Component
            implementation(libs.androidx.navigation.compose)
            implementation(libs.androidx.navigation.runtime.ktx)

            implementation(libs.room.ktx)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            // JSON Serialization for parsing cocktails data
            implementation(libs.kotlinx.serialization.json)

            // Multiplatform image loading
            implementation(libs.coil.compose)

            // Koin core for dependency injection
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)

            // Room dependencies for Android
            implementation(libs.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidUnitTest.dependencies {
            implementation(libs.junit)
            implementation(libs.androidx.test.ext.junit)
        }
    }
}

android {
    namespace = "com.devphill.cocktails"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.devphill.cocktails"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        getByName("debug") {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

// Configure JaCoCo
jacoco {
    toolVersion = "0.8.11"
}

/**
 * JaCoCo Test Coverage Configuration
 *
 * This configuration provides code coverage reporting for the Android app module.
 *
 * Available Tasks:
 * - `./gradlew jacocoTestReport` - Generates coverage report after running tests
 * - `./gradlew jacocoTestReportAndOpen` - Generates report and opens it in browser
 *
 * Report Locations:
 * - HTML Report: build/reports/jacoco/jacocoTestReport/html/index.html
 * - XML Report: build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml
 *
 * The configuration includes:
 * - Coverage for commonMain and androidMain source sets
 * - Exclusion of generated classes, R classes, and test files
 * - HTML and XML report generation (CSV disabled)
 * - Automatic dependency on testDebugUnitTest task
 *
 * Prerequisites:
 * - Run unit tests first: `./gradlew testDebugUnitTest`
 * - Or use the jacocoTestReport task which includes this dependency
 *
 * Coverage Scope:
 * - Includes: All production code in commonMain and androidMain
 * - Excludes: Test files, generated code, Android R classes, BuildConfig, Manifest files
 */

// Custom task to generate unified test coverage report
tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/lambda$*.class",
        "**/lambda.class",
        "**/*lambda.class",
        "**/*lambda*.class",
        "**/*\$WhenMappings.*",
        "**/*\$WhenMappings\$*.*",
        "**/serializer.*",
        "**/*\$\$serializer.*"
    )

    val debugTree = fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }

    val mainSrc = "${project.projectDir}/src/commonMain/kotlin"
    val androidMainSrc = "${project.projectDir}/src/androidMain/kotlin"

    sourceDirectories.setFrom(files(listOf(mainSrc, androidMainSrc)))
    classDirectories.setFrom(files(listOf(debugTree)))
    executionData.setFrom(fileTree(layout.buildDirectory.get()) {
        include("jacoco/testDebugUnitTest.exec", "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
    })
}

/**
 * Convenience task to generate coverage report and automatically open it in the default browser.
 *
 * Usage: `./gradlew jacocoTestReportAndOpen`
 *
 * This task will:
 * 1. Run unit tests (via jacocoTestReport dependency)
 * 2. Generate the coverage report
 * 3. Open the HTML report in your default browser (macOS only)
 */
// Task to generate coverage report and open it
tasks.register("jacocoTestReportAndOpen") {
    dependsOn("jacocoTestReport")

    doLast {
        val reportPath = "${layout.buildDirectory.get()}/reports/jacoco/jacocoTestReport/html/index.html"
        if (file(reportPath).exists()) {
            exec {
                commandLine("open", reportPath)
            }
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
}
