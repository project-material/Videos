plugins {
    id("com.android.library")
}

android {
    namespace = "com.projectmaterial.preference"
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
    implementation(project(":components"))
    implementation("androidx.core:core:1.13.1")
    implementation("androidx.preference:preference:1.2.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.android.material:material:1.13.0-alpha08")
}