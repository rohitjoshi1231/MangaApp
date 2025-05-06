plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    alias(libs.plugins.google.gms.google.services)
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

    aaptOptions {
        noCompress.add("tflite")
    }
}

dependencies {


    // Media Pipe
    implementation(libs.tasks.vision)

    implementation(libs.accompanist.permissions)

    // CameraX dependencies for live camera feed
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    implementation(libs.coil.compose)

    implementation(libs.volley)

    // glide
    implementation(libs.glide)
    implementation(libs.firebase.database)

    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.compiler)

    implementation(libs.accompanist.placeholder.material)

    implementation(libs.compose.shimmer)
    implementation(libs.shimmer)
    implementation(libs.androidx.datastore.preferences)

    // ✅ Use latest stable Hilt version (2.48.1 or higher)
    implementation(libs.dagger.hilt.android)
    kapt(libs.hilt.compiler)

// ✅ Compose integration (keep this if you're using Jetpack Compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    // Room compiler with KSP
    kapt(libs.androidx.room.compiler)

    // AndroidX Libraries for Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.ui)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(project(":core"))

    // Test Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.tooling.preview)
}
