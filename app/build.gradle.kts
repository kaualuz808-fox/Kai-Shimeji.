
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.kaishimeji"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kaishimeji"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "0.1"
    }
}
