import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

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

        manifestPlaceholders["amap_api_key"] = gradleLocalProperties(rootDir).getProperty("amap_api_key")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles("proguard-rules.pro")
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file(gradleLocalProperties(rootDir).getProperty("keystore.debug.filename"))
            storePassword = gradleLocalProperties(rootDir).getProperty("keystore.debug.storePassword")
            keyAlias = gradleLocalProperties(rootDir).getProperty("keystore.debug.keyAlias")
            keyPassword = gradleLocalProperties(rootDir).getProperty("keystore.debug.keyPassword")
        }
        create("release") {
            storeFile = file(gradleLocalProperties(rootDir).getProperty("keystore.release.filename"))
            storePassword = gradleLocalProperties(rootDir).getProperty("keystore.release.storePassword")
            keyAlias = gradleLocalProperties(rootDir).getProperty("keystore.release.keyAlias")
            keyPassword = gradleLocalProperties(rootDir).getProperty("keystore.release.keyPassword")
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
    implementation(project(Modules.user))
    implementation(project(Modules.account))
    implementation(project(Modules.event))
    implementation(project(Modules.feed))
    implementation(project(Modules.comment))
    implementation(project(Modules.group))
    implementation(project(Modules.poi))

    compileOnly(project(Modules.accountApi))
    compileOnly(project(Modules.userApi))
    compileOnly(project(Modules.eventApi))

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