import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    //HILT
//    id("com.google.devtools.ksp") version "2.1.20-1.0.32"
//    id("com.google.dagger.hilt.android")

    id ("kotlin-kapt") // <-- This one is for Room compiler
}

android {
    namespace = "com.example.newsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.newsapp"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // HILT
//    implementation("com.google.dagger:hilt-android:2.56.1")
//    ksp("com.google.dagger:hilt-android-compiler:2.56.1")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Room
    implementation ("androidx.room:room-runtime:2.7.1")
    kapt ("androidx.room:room-compiler:2.7.1")
    implementation ("androidx.room:room-ktx:2.7.1")

    // Koin
    implementation ("io.insert-koin:koin-android:3.5.3")
    implementation ("io.insert-koin:koin-androidx-compose:3.5.3")


}