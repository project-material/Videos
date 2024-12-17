plugins {
    id("com.android.library")
}

android {
    namespace = "com.projectmaterial.widget"
    compileSdk = 34
    
    defaultConfig {
        minSdk = 31
        targetSdk = 34
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.13.0-alpha08")
}