import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

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

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)

        dependencies {
            androidTestImplementation(libs.compose.ui.test.junit4)
            debugImplementation(libs.compose.ui.test.manifest)
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
            freeCompilerArgs.add("-Xexpected-actual-classes")
        }
    }

    // Add global compiler options for all targets
    compilerOptions {
        freeCompilerArgs.add("-Xexpected-actual-classes")
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

            // DateTime handling
            implementation(libs.kotlinx.datetime)

            // Multiplatform image loading
            implementation(libs.coil.compose)

            // Koin core for dependency injection
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)

            // Multiplatform Settings for local storage
            implementation(libs.multiplatform.settings)

            // Room dependencies for Android
            implementation(libs.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)

            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }

        androidUnitTest.dependencies {
            implementation(libs.junit)
            implementation(libs.androidx.test.ext.junit)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.compose.ui.test.junit4)
            implementation(libs.compose.ui.test.manifest)
        }

        androidInstrumentedTest.dependencies {
            implementation(libs.androidx.test.ext.junit)
            implementation(libs.androidx.test.espresso.core)
            implementation(libs.compose.ui.test.junit4)
            implementation(libs.compose.ui.test.manifest)
            implementation(libs.kotlinx.coroutines.test)
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
        testOptions.unitTests.isIncludeAndroidResources = true
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
     * - Automatic dependency on both unit and instrumented tests
     *
     * Prerequisites:
     * - Tests are run automatically when you call the jacocoTestReport task
     *
     * Coverage Scope:
     * - Includes: All production code in commonMain and androidMain
     * - Excludes: Test files, generated code, Android R classes, BuildConfig, Manifest files
     */

    // Make sure test tasks always run and are not considered up-to-date
    tasks.withType<Test>().configureEach {
        outputs.upToDateWhen { false }
    }

// Custom task to generate unified test coverage report
    tasks.register<JacocoReport>("jacocoTestReport") {
        // Depend on both unit tests and connected Android tests
        dependsOn("testDebugUnitTest", "connectedDebugAndroidTest")

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
            "**/lambda$*.class",
            "**/lambda.class",
            "**/*lambda.class",
            "**/*lambda*.class",
            "**/*\$WhenMappings.*",
            "**/*\$WhenMappings\$*.*",
            "**/serializer.*",
            "**/*\$\$serializer.*"
        )

        // Include both debug classes and commonMain classes compiled for Android
        val debugTree = fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/debug") {
            exclude(fileFilter)
        }

        val androidDebugTree = fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/androidDebug") {
            exclude(fileFilter)
        }

        val debugAndroidTestTree = fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/debugAndroidTest") {
            exclude(fileFilter)
        }

        // Additional class directories for Kotlin Multiplatform
        val kotlinClassesTree = fileTree("${layout.buildDirectory.get()}/classes/kotlin") {
            exclude(fileFilter)
        }

        val mainSrc = "${project.projectDir}/src/commonMain/kotlin"
        val androidMainSrc = "${project.projectDir}/src/androidMain/kotlin"

        sourceDirectories.setFrom(files(listOf(mainSrc, androidMainSrc)))
        classDirectories.setFrom(files(listOf(debugTree, androidDebugTree, debugAndroidTestTree, kotlinClassesTree)))
        executionData.setFrom(fileTree(layout.buildDirectory.get()) {
            include(
                // Unit test execution data
                "jacoco/testDebugUnitTest.exec",
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                // Instrumented test execution data
                "outputs/code_coverage/debugAndroidTest/connected/*coverage.ec",
                "outputs/code_coverage/debugAndroidTest/**/*.ec",
                // Additional Android test coverage paths
                "jacoco/testDebugAndroidTest.exec",
                // Kotlin multiplatform coverage
                "jacoco/*.exec"
            )
        })
    }

    /**
     * Convenience task to generate coverage report and automatically open it in the default browser.
     *
     * Usage: `./gradlew jacocoTestReportAndOpen`
     *
     * This task will:
     */
    tasks.register("jacocoTestReportAndOpen") {
        dependsOn("jacocoTestReport")

        doLast {
            val reportPath =
                "${layout.buildDirectory.get()}/reports/jacoco/jacocoTestReport/html/index.html"
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
}
