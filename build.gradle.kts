plugins {
    // Needed for SafeArgs

    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
//    id("androidx.navigation.safeargs.kotlin") version "2.9.6" apply false
//
//    // Android + Kotlin plugins
//    id("com.android.application") version "8.7.2" apply false
//    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
//    id("com.google.dagger.hilt.android") version "2.52" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}
