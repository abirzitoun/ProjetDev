plugins {
    alias(libs.plugins.android.application)

}


android {
    namespace = "com.example.pharmacie"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pharmacie"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //scalable size

    // Scalable size
    implementation(libs.sdp.android)

    // Circular ImageView
    implementation(libs.circleimageview)

    implementation(libs.mysql.connector.java)
// Swipe Reveal Layout
    implementation(libs.swipereveallayout)


}

