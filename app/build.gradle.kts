plugins {
    // Application Specific Plugins
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.WMRouter)

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
    implementation(project(Modules.user))

    implementation(Libraries.MultiDex.multiDex)

    implementation(Libraries.ConstraintLayout.constraintLayout)
    implementation(Libraries.Navigation.fragment)
    implementation(Libraries.Navigation.ui)
    implementation(Libraries.Navigation.fragmentKtx)
    implementation(Libraries.Navigation.uiKtx)
    implementation(Libraries.Lifecycle.liveDataKtx)
    implementation(Libraries.Lifecycle.viewModelKtx)
    implementation(Libraries.Paging3.runtime)
    implementation(Libraries.Room.ktx)

    kapt(Libraries.Dagger.compiler)
    kapt(Libraries.Dagger.processor)

    kapt(Libraries.Epoxy.compiler)
    kapt(Libraries.Room.compiler)
}