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

        vectorDrawables {
            useSupportLibrary = true
        }
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
    compileOnly(project(Modules.baseFramework))
    compileOnly(project(Modules.dataAndroid))

    compileOnly(project(Modules.accountApi))
    compileOnly(project(Modules.userApi))
    compileOnly(project(Modules.composerApi))
    compileOnly(project(Modules.framework))

    kapt(Libraries.Epoxy.compiler)
    kapt(Libraries.Dagger.compiler)
    kapt(Libraries.Dagger.processor)
}