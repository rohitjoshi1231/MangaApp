plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")

}

android {
    compileSdk = 34

    namespace = "com.alpha.mangaapp"
    defaultConfig {
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }

    kotlinOptions {
        jvmTarget = "17" // Kotlin JVM target set to 17
    }
    // Ensure Java compatibility is also set to 17
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {

    // Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.hilt.navigation.compose)

    // Data storage
    implementation(libs.androidx.datastore.preferences)

    // Volley
    implementation(libs.volley)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    // Room compiler with KSP
    ksp(libs.androidx.room.compiler)


    // AndroidX Libraries for UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.ui)
    implementation(libs.androidx.material3)

    // Test Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
}
