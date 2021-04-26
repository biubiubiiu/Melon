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

    ndkVersion = AndroidSDK.ndkVersion

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

    externalNativeBuild {
        cmake {
            path = File("${projectDir}/src/main/cpp/CMakeLists.txt")
        }
    }
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

dependencies {
    api(project(Modules.commonUi))
    api(project(Modules.commonUtils))
    api(project(Modules.runtimePermissions))
    api(project(Modules.location))

    api(project(Modules.data))

    api(Libraries.AndroidX.AppCompat.appCompat)

    api(Libraries.Epoxy.paging)

    api(Libraries.Retrofit.gsonConverter)
    api(Libraries.OkHttp.okHttp)

    kapt(Libraries.Dagger.compiler)
}