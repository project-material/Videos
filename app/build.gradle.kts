plugins {
    id("com.android.application")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.projectmaterial.videos"
    compileSdk = 34
    buildToolsVersion = "34.0.4"
    
    defaultConfig {
        applicationId = "com.projectmaterial.videos"
        minSdk = 31
        targetSdk = 34
        versionCode = 1023
        versionName = "1.0.2-beta03"
        
        ndk {
            abiFilters += listOf("arm64-v8a")
        }
        
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    
    dependenciesInfo {
        includeInApk = false
    }
    
    packaging {
        dex {
            useLegacyPackaging = false
        }
    
        jniLibs {
            useLegacyPackaging = false
        }
        
        resources {
            excludes += "META-INF/**"
            excludes += "com/**"
            excludes += "kotlin/**"
            excludes += "okhttp3/**"
            excludes += "**.properties"
            excludes += "**.bin"
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
    implementation(project(":preference"))
    implementation("androidx.activity:activity:1.9.3")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.collection:collection:1.4.5")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("androidx.core:core:1.13.1")
    implementation("androidx.fragment:fragment:1.6.2")
    implementation("androidx.navigation:navigation-fragment:2.8.4")
    implementation("androidx.navigation:navigation-ui:2.8.4")
    implementation("androidx.preference:preference:1.2.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.work:work-runtime:2.9.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.android.material:material:1.13.0-alpha08")
}