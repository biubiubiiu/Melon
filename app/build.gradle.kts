plugins {
    // Application Specific Plugins
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.kotlinAndroidExtensions)

    // Internal Script plugins
    id(ScriptPlugins.compilation)
    id(ScriptPlugins.infrastructure)
}

android {
    compileSdkVersion(AndroidSDK.compile)
    buildToolsVersion(AndroidSDK.buildToolsVersion)

    defaultConfig {
        minSdkVersion(AndroidSDK.min)
        targetSdkVersion(AndroidSDK.target)

        applicationId = AndroidClient.appId
        versionCode = AndroidClient.versionCode
        versionName = AndroidClient.versionName
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles("proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.ktxCore)
    implementation(Libraries.appCompat)
    implementation(Libraries.material)
    implementation(Libraries.constraintLayout)
}