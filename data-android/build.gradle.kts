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
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

dependencies {
    api(project(Modules.data))

    api(Libraries.Room.runtime)
    api(Libraries.Room.ktx)

    kapt(Libraries.Room.compiler)
    kapt(Libraries.Dagger.compiler)
}