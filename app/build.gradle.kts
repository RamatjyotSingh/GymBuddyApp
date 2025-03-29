plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "comp3350.gymbuddy"
    compileSdk = 34

    defaultConfig {
        applicationId = "comp3350.gymbuddy"
        minSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.timber)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.json)
    implementation(libs.material)
    // RecyclerView & CardView
    implementation(libs.recyclerview)
    implementation(libs.cardview)
    implementation(libs.hsqldb)
    implementation(libs.hsqldb)

    implementation(libs.androidx.core.splashscreen)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.mockito.core)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.androidx.test.runner) // ✅ Fixed reference
    androidTestImplementation(libs.androidx.test.rules) // ✅ Fixed reference
    androidTestImplementation(libs.espresso.contrib) // Required for RecyclerViewActions
    androidTestImplementation(libs.espresso.intents) // Optional for testing intents


}