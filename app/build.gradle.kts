import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.3.6"
}

android {
    namespace = "com.example.rulesreviewer"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    val localProperties = Properties().apply {
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { load(it) }
        }
    }

    defaultConfig {
        applicationId = "com.example.rulesreviewer"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "OPENWEATHER_API_KEY", "\"${localProperties.getProperty("OPENWEATHER_API_KEY") ?: ""}\"")
        buildConfigField("String", "GOOGLE_OAUTH_CLIENT_SECRET", "\"${localProperties.getProperty("GOOGLE_OAUTH_CLIENT_SECRET") ?: ""}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // OkHttp for networking with secure configuration (Rule R8)
    implementation(libs.okhttp)
    // Google Play Services Auth for secure Google Sign-In (Rule R8)
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    // Jetpack Biometric library for secure authentication (Rule R8)
    implementation(libs.androidx.biometric)
    // WorkManager for secure and reliable background synchronization (Rule R8)
    implementation(libs.androidx.work.runtime)
    // Retrofit for secure network operations (Rule R8)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    // Gson for JSON parsing and serialization (Rule R8)
    implementation(libs.gson)
    // Firebase Crashlytics for secure error reporting (Rule R8)
    implementation(libs.firebase.crashlytics)
    // Jetpack Security library for EncryptedSharedPreferences (Rule R2)
    implementation(libs.androidx.security.crypto)
    // Room persistence library for local storage (Rule R8)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}