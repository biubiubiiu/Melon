import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    // Application Specific Plugins
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.kotlinKapt)

    // Firebase
    id(FirebaseService.crashlytics)

    // Internal Script plugins
    id(ScriptPlugins.compilation)
    id(ScriptPlugins.infrastructure)
}

android {
    compileSdkVersion(AndroidSDK.compile)
    buildToolsVersion(AndroidSDK.buildToolsVersion)

    ndkVersion = AndroidSDK.ndkVersion

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
    implementation(project(Modules.gallery))
    implementation(project(Modules.composer))
    implementation(project(Modules.settings))

    implementation(project(Modules.accountApi))
    implementation(project(Modules.userApi))
    implementation(project(Modules.eventApi))
    implementation(project(Modules.poiApi))
    implementation(project(Modules.composerApi))

    implementation(Libraries.MultiDex.multiDex)

    implementation(platform(Libraries.Firebase.bom))
    implementation(Libraries.Firebase.analytics)
    implementation(Libraries.Firebase.crashlytics)

    implementation(Libraries.Cropping.uCrop)

    kapt(Libraries.Dagger.compiler)
    kapt(Libraries.Dagger.processor)
}

apply(plugin = FirebaseService.googleService)