
plugins {
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {

    namespace = "com.daryush.thesis"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.daryush.thesis"
        minSdk = 29
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        apply(plugin = "kotlin-kapt")
        apply(plugin = "kotlin-android")
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.core)
    implementation(libs.vision.common)
    implementation(libs.play.services.mlkit.barcode.scanning)
    implementation(libs.zxing.android.embedded)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.core.v341)


//    val room_version = "2.6.1"
//    implementation("androidx.room:room-runtime:$room_version")
//    annotationProcessor("androidx.room:room-compiler:$room_version")


    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
}