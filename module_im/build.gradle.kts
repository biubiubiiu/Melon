plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)

    id(ScriptPlugins.infrastructure)
}

android {
    compileSdkVersion(AndroidSDK.compile)
    buildToolsVersion(AndroidSDK.buildToolsVersion)

    ndkVersion = AndroidSDK.ndkVersion

    defaultConfig {
        minSdkVersion(AndroidSDK.min)
        targetSdkVersion(AndroidSDK.target)

        versionCode = AndroidClient.versionCode
        versionName = AndroidClient.versionName

        ndk {
            abiFilters.addAll(listOf("armeabi", "armeabi-v7a", "arm64-v8a"))
        }

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

dependencies {
    api("cn.jiguang.sdk:jmessage:2.9.2")
    api("cn.jiguang.sdk:jcore:2.3.0")

    compileOnly(project(Modules.baseFramework))
    compileOnly(project(Modules.runtimePermissions))

    kapt(Libraries.Dagger.compiler)
    kapt(Libraries.Dagger.processor)
}