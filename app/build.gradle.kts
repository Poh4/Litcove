plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.androidx.navigation.safeargs.kotlin)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.dagger.hilt.android)
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.litcove.litcove"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.litcove.litcove"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                @Suppress("UnstableApiUsage")
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.circleimageview)
    implementation(libs.compressor)
    implementation(libs.converter.gson)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.dynamic.links)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.glide)
    implementation(libs.googleid)
    implementation(libs.gson)
    implementation(libs.hilt.android)
    implementation(libs.logging.interceptor)
    implementation(libs.lottie)
    implementation(libs.material)
    implementation(libs.play.services.auth)
    implementation(libs.retrofit)
    implementation(libs.firebase.storage.ktx)
    kapt(libs.hilt.android.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}