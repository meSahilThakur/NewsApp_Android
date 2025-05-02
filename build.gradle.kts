// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    id("com.google.dagger.hilt.android") version "2.56.1" apply false // Add Hilt plugin
    id("com.google.devtools.ksp") version "2.1.20-1.0.32" apply false // Add KSP plugin (for Room, Hilt, etc.)

}