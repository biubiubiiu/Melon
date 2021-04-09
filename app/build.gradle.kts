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
    implementation(project(Modules.recommend))
    implementation(project(Modules.follow))
    implementation(project(Modules.user))
    implementation(project(Modules.account))
    implementation(project(Modules.feed))
    implementation(project(Modules.comment))
    implementation(project(Modules.group))

    compileOnly(project(Modules.accountApi))
    compileOnly(project(Modules.userApi))

    implementation(Libraries.MultiDex.multiDex)

    implementation(Libraries.Navigation.fragment)
    implementation(Libraries.Navigation.ui)
    implementation(Libraries.Navigation.fragmentKtx)
    implementation(Libraries.Navigation.uiKtx)
    implementation(Libraries.Paging3.runtime)

    kapt(Libraries.Dagger.compiler)
    kapt(Libraries.Dagger.processor)

    kapt(Libraries.Epoxy.compiler)
}