plugins {
  id("com.android.application") version "8.6.1"
  id("org.jetbrains.kotlin.android") version "2.0.20"
}

android {
  namespace = "de.alex.autostartride"
  compileSdk = 34

  defaultConfig {
    applicationId = "de.alex.autostartride"
    minSdk = 26
    targetSdk = 34
    versionCode = 1
    versionName = "1.0.0"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
    debug {
      isMinifyEnabled = false
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
}

dependencies {
  implementation("androidx.core:core-ktx:1.13.1")
  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("com.google.android.material:material:1.12.0")

  // DataStore (Preferences)
  implementation("androidx.datastore:datastore-preferences:1.1.1")

  // Coroutines & Lifecycle
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
  implementation("androidx.activity:activity-ktx:1.9.2")

  // karoo-ext (kommt über includeBuild + dependencySubstitution)
  implementation("io.hammerhead:karoo-ext")
}

