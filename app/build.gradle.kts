plugins { id("com.android.application") }

android {
    namespace = "cv.wallima.a56toolkit"
    compileSdk = 35

    defaultConfig {
        applicationId = "cv.wallima.a56toolkit"
        minSdk = 26
        targetSdk = 35
        versionCode = 8
        versionName = "3.0"
    }

    buildFeatures { viewBinding = true }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity:1.9.3")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
}
