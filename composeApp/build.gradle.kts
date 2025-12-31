import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

gradle.projectsEvaluated {
    val composeGenTasks = listOf(
        "generateResourceAccessorsForAndroidDebug",
        "generateResourceAccessorsForAndroidMain",
        "generateActualResourceCollectorsForAndroidMain",
        "generateComposeResClass",
        "generateResourceAccessorsForCommonMain",
        "generateExpectResourceCollectorsForCommonMain"
    )

    tasks.matching { it.name == "kspDebugKotlinAndroid" }.configureEach {
        dependsOn(composeGenTasks)
        mustRunAfter(composeGenTasks)
    }

    tasks.matching { it.name.startsWith("ksp") && it.name.endsWith("KotlinAndroid") }
        .configureEach {
            dependsOn(composeGenTasks)
            mustRunAfter(composeGenTasks)
        }
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    // Desktop JVM target
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    // iOS
    val iosArm64 = iosArm64()
    val iosSimulatorArm64 = iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtime.compose)
                implementation(libs.navigation.compose)

                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)

                implementation(libs.room.runtime)
                implementation(libs.sqlite.bundled)

                implementation(libs.kotlinx.serialization.json)

                implementation(libs.compose.material.icons.extended)
                implementation(libs.compose.material.icons.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.koin.android)
            }
        }

        // Desktop JVM source set
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
            }
        }

        // Optional: shared iOSMain if you want
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspDesktop", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspCommonMainMetadata", libs.room.compiler)
}

android {
    namespace = "com.sadeghtahani.notebox"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.sadeghtahani.notebox"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.sadeghtahani.notebox.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.sadeghtahani.notebox"
            packageVersion = "1.0.0"
        }
    }
}