plugins {
    // Library Specific Plugins
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)

    id(ScriptPlugins.infrastructure)
}

android {
    compileSdkVersion(AndroidSDK.compile)
    buildToolsVersion(AndroidSDK.buildToolsVersion)

    defaultConfig {
        minSdkVersion(AndroidSDK.min)
        targetSdkVersion(AndroidSDK.target)

        versionCode = AndroidClient.versionCode
        versionName = AndroidClient.versionName
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles("proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    compileOnly(project(Modules.commonUi))
    compileOnly(project(Modules.commonUtils))
    compileOnly(Libraries.kotlinStdLib)
    compileOnly(Libraries.AndroidX.Core.ktx)
    compileOnly(Libraries.AndroidX.AppCompat.appCompat)
    compileOnly("androidx.activity:activity-ktx:1.2.0-beta02")
}