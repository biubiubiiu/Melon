plugins {
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
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

dependencies {
    implementation(project(Modules.base))
    api(project(Modules.data))
    implementation(project(Modules.dataAndroid))

    kapt(Libraries.daggerCompiler)

    api(Libraries.paging3Common)
    implementation(Libraries.paging3)
}