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
    api(project(Modules.commonUi))
    api(project(Modules.commonUtils))

    api(Libraries.AndroidX.AppCompat.appCompat)

    api(Libraries.Room.runtime)

    api(Libraries.Mavericks.mavericks)

    api(Libraries.Epoxy.paging)

    api(Libraries.Retrofit.gsonConverter)
    api(Libraries.Retrofit.retrofit)
    api(Libraries.OkHttp.okHttp)

    kapt(Libraries.Epoxy.compiler)
    kapt(Libraries.Dagger.compiler)
}