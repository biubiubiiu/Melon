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

    buildFeatures {
        viewBinding = true
    }
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

dependencies {
    compileOnly(project(Modules.baseFramework))
    compileOnly(project(Modules.commonAnimation))

    compileOnly(project(Modules.composerApi))
    compileOnly(project(Modules.runtimePermissions))

    compileOnly(Libraries.AndroidX.Core.ktx)

    kapt(Libraries.Epoxy.compiler)
    kapt(Libraries.Dagger.compiler)
    kapt(Libraries.Dagger.processor)
}