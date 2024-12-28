plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.projectptc"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.projectptc"
        minSdk = 24
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
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation ("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation ("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")
    implementation ("com.jjoe64:graphview:4.2.2")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.github.SeptiawanAjiP:AndroidChartJS:1.0.5")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("org.tensorflow:tensorflow-lite:2.12.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}