plugins {
    // Application Specific Plugins
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.kotlinKapt)

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
        multiDexEnabled = true

        applicationId = AndroidClient.appId
        versionCode = AndroidClient.versionCode
        versionName = AndroidClient.versionName

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
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
    generateStubs = true
}

dependencies {
    implementation(project(Modules.baseFramework))
    implementation(project(Modules.dataAndroid))
    implementation(project(Modules.base))
    implementation(project(Modules.domain))
    implementation(project(Modules.recommend))
    implementation(project(Modules.follow))

    implementation(Libraries.multiDex)

    implementation(Libraries.constraintLayout)
    implementation(Libraries.navigationFragment)
    implementation(Libraries.navigationUI)
    implementation(Libraries.navigationFragmentKtx)
    implementation(Libraries.navigationUIKtx)
    implementation(Libraries.lifecycleLivedataKtx)
    implementation(Libraries.lifecycleViewModelKtx)
    implementation(Libraries.paging3)
    implementation(Libraries.roomKtx)

    kapt(Libraries.daggerCompiler)
    kapt(Libraries.daggerProcessor)

    kapt(Libraries.epoxyCompiler)
    kapt(Libraries.roomCompiler)
}