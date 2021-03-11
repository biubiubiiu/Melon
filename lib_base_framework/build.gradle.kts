plugins {
    // Library Specific Plugins
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)

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

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

dependencies {
    api(project(Modules.data))

    api(Libraries.ktxCore)
    api(Libraries.appCompat)
    api(Libraries.recyclerView)
    api(Libraries.material)

    api(Libraries.roomRuntime)
    api(Libraries.daggerAndroidSupport)

    api(Libraries.mavericks)
    api(Libraries.epoxyRuntime)
    api(Libraries.epoxyPaging)

    api(Libraries.coil)

    api(Libraries.okHttp)

    kapt(Libraries.epoxyCompiler)
}