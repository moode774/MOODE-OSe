plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }
android { namespace = "com.moode.tvos"; compileSdk = 35
    defaultConfig { applicationId = "com.moode.tvos"; minSdk = 26; targetSdk = 34; versionCode = 1; versionName = "0.1" }
}